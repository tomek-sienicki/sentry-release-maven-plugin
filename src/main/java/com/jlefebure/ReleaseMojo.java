package com.jlefebure;


import com.jlefebure.client.SentryHttpClient;
import com.jlefebure.model.release.SentryCommit;
import com.jlefebure.model.release.SentryRef;
import com.jlefebure.model.release.SentryReleaseRequestDTO;
import com.jlefebure.model.release.SentryReleaseResponseDTO;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create or update a release on Sentry with the version specified in the pom.xml project file.
 *
 * @goal release
 */
public class ReleaseMojo extends AbstractMojo {

    private SentryHttpClient sentryHttpClient;

    /**
     * Project instance
     *
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Repository name as set on Sentry instance
     *
     * @parameter
     * @required
     */
    private String repository;

    /**
     * Sentry API URL. Could be an instance hosted on the cloud or a self-hosted one.
     *
     * @parameter
     * @required
     */
    private String sentryUrl;

    /**
     * Authentication token of the Sentry API.
     *
     * @parameter
     * @required
     */
    private String authToken;

    /**
     * Organization of the Sentry instance
     *
     * @parameter
     * @required
     */
    private String organization;

    /**
     * List of projects on which the release should be deployed. Must at least contain one project
     *
     * @parameter
     * @required
     */
    private List<String> projects;

    /**
     * Version which should be deployed.
     * If not provided, the default value will be the project version set in the pom.xml without the "-SNAPSHOT" prefix
     *
     * @parameter
     */
    private String version;

    private Repository buildScm() throws IOException {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();

        return repositoryBuilder
                .setGitDir(new File(".git"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .setMustExist(true)
                .build();
    }

    public void execute() throws MojoExecutionException {
        try {
            if (projects == null || projects.isEmpty()) {
                MojoExecutionException mojoExecutionException = new MojoExecutionException("One project should at least be configured in the plugin configuration");
                getLog().error(mojoExecutionException);
                throw mojoExecutionException;
            }

            sentryHttpClient = new SentryHttpClient(sentryUrl, authToken, organization, getLog());
            Repository git = this.buildScm();
            String describe = Git
                    .wrap(git)
                    .describe()
                    .call();

            //Get last commit on last release (get last tag)
            String lastTag = describe.split("-")[0];
            this.getLog().info("Last tag was " + lastTag);

            ObjectId lastCommitRelease = git.getTags().get(lastTag).getObjectId();

            this.getLog().info("Last release commit was " + lastCommitRelease.getName());

            //Get current commit
            Ref currentCommit = git.getRefDatabase().findRef("HEAD");

            this.getLog().info("Current commit is " + currentCommit.getObjectId().getName());

            if (version == null) {
                version = project.getVersion();
                version = version.replaceAll("-SNAPSHOT", "");
            }
            this.getLog().info("Current release is " + version);

            //Send sentry create release with elements

            SentryRef ref = createRef(lastCommitRelease.getName(), currentCommit);
            SentryReleaseResponseDTO existingRelease = sentryHttpClient.getRelease(version);
            if (existingRelease == null) {
                SentryReleaseRequestDTO sentryReleaseRequestDTO = createRelease(ref);
                SentryReleaseResponseDTO release1 = sentryHttpClient.createRelease(sentryReleaseRequestDTO);

                getLog().info("Release " + release1.getVersion() + " has been created");

            } else {
                //A release already exists, then we set the current commit
                SentryReleaseResponseDTO release = updateRelease(ref, existingRelease);
                getLog().info("Release " + release.getVersion() + " has been updated");
            }
        } catch (IOException | GitAPIException e) {
            MojoExecutionException mojoExecutionException = new MojoExecutionException("Error while constructing local git repo", e);
            this.getLog().error(mojoExecutionException);
            throw mojoExecutionException;
        }

    }

    private SentryReleaseResponseDTO updateRelease(SentryRef ref, SentryReleaseResponseDTO existingRelease) throws IOException {
        SentryReleaseRequestDTO updateRelease = new SentryReleaseRequestDTO();
        updateRelease.setProjects(projects);
        updateRelease.setVersion(existingRelease.getVersion());
        updateRelease.setRefs(Arrays.asList(ref));
        return sentryHttpClient.createRelease(updateRelease);
    }

    private SentryReleaseRequestDTO createRelease(SentryRef ref) {
        //No release exists with this version, then let's create it
        SentryReleaseRequestDTO sentryReleaseRequestDTO = new SentryReleaseRequestDTO();
        sentryReleaseRequestDTO.setVersion(version);
        sentryReleaseRequestDTO.setRefs(Arrays.asList(ref));
        sentryReleaseRequestDTO.setProjects(projects);
        return sentryReleaseRequestDTO;
    }

    private List<SentryCommit> getCommits(Repository git, ObjectId lastCommitRelease) throws GitAPIException, MissingObjectException, IncorrectObjectTypeException {
        Iterable<RevCommit> call = Git.wrap(git).log().add(lastCommitRelease).call();
        List<SentryCommit> commits = new ArrayList<>();

        for (RevCommit rev : call) {
            this.getLog().info("Add commit " + rev.getId().getName());
            SentryCommit commit = this.createCommit(rev);
            commits.add(commit);
        }

        return commits;
    }

    private SentryCommit createCommit(RevCommit commit) {
        SentryCommit sentryCommit = new SentryCommit();
        sentryCommit.setId(commit.getId().getName());
        sentryCommit.setAuthorEmail(commit.getAuthorIdent().getEmailAddress());
        sentryCommit.setAuthorName(commit.getAuthorIdent().getName());
        sentryCommit.setMessage(commit.getFullMessage());
        sentryCommit.setRepository(this.repository);

        return sentryCommit;
    }

    private SentryRef createRef(String lastCommitRelease, Ref currentCommit) {
        SentryRef sentryRef = new SentryRef();
        sentryRef.setPreviousCommit(lastCommitRelease);
        sentryRef.setCommit(currentCommit.getObjectId().getName());
        sentryRef.setRepository(repository);
        return sentryRef;
    }
}

package com.jlefebure;


import com.jlefebure.client.SentryHttpClient;
import com.jlefebure.model.deploy.SentryDeployRequestDTO;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

/**
 * Create a deploy on the specified environment for the version set in the project pom.xml. This phase should be executed
 * after the release phase.
 *
 * If the version has -SNAPSHOT suffic on it, then the suffix is deleted.
 *
 * @goal deploy
 */
public class DeployMojo extends AbstractMojo {

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
     * @parameter
     * @required
     */
    private String repository;

    /**
     * Sentry API URL. Could be an instance hosted on the cloud or a self-hosted one.
     * @parameter
     * @required
     */
    private String sentryUrl;

    /**
     * Authentication token of the Sentry API.
     * @parameter
     * @required
     */
    private String authToken;

    /**
     * Organization of the Sentry instance
     * @parameter
     * @required
     */
    private String organization;

    /**
     * Environment on which the deploy is created
     * @parameter property="sentry.environment"
     * @required
     */
    private String environment;

    /**
     * Name of the release. Optional parameter
     * @parameter
     */
    private String releaseName;

    /**
     * The optional url that points to the deploy
     * @parameter
     */
    private String url;

    /**
     * Version which should be deployed.
     * If not provided, the default value will be the project version set in the pom.xml without the "-SNAPSHOT" prefix
     * @parameter
     */
    private String version;

    public void execute() throws MojoExecutionException {
        try {
            sentryHttpClient = new SentryHttpClient(sentryUrl, authToken, organization, getLog());

            if (this.version == null) {
                version = project.getVersion();
                version = version.replaceAll("-SNAPSHOT", "");
            }

            this.getLog().info("Current release is " + version);
            createDeploy(version);
        } catch (IOException e) {
            MojoExecutionException mojoExecutionException = new MojoExecutionException("Error while creating deploy", e);
            this.getLog().error(mojoExecutionException);
            throw mojoExecutionException;
        }

    }

    private void createDeploy(String version) throws IOException {
        SentryDeployRequestDTO sentryDeployRequestDTO = new SentryDeployRequestDTO();
        sentryDeployRequestDTO.setEnvironment(this.environment);
        if (releaseName != null) {
            sentryDeployRequestDTO.setName(releaseName);
        }
        if (url != null) {
            sentryDeployRequestDTO.setName(url);
        }

        this.sentryHttpClient.createDeploy(sentryDeployRequestDTO, version);
    }

}

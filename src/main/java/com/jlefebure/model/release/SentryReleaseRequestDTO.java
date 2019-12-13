package com.jlefebure.model.release;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SentryReleaseRequestDTO {
    @JsonProperty("version")
    private String version;
    @JsonProperty("refs")
    private List<SentryRef> refs;
    @JsonProperty("projects")
    private List<String> projects;
    @JsonProperty("url")
    private String url;
    @JsonProperty("commits")
    private List<SentryCommit> commits;

    public void setCommits(List<SentryCommit> commits) {
        this.commits = commits;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<SentryRef> getRefs() {
        return refs;
    }

    public void setRefs(List<SentryRef> refs) {
        this.refs = refs;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

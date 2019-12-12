
package com.jlefebure.model.release;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SentryReleaseResponseDTO {

    @JsonProperty("commitCount")
    private Integer commitCount;
    @JsonProperty("dateCreated")
    private String dateCreated;
    @JsonProperty("dateReleased")
    private Object dateReleased;
    @JsonProperty("deployCount")
    private Integer deployCount;
    @JsonProperty("firstEvent")
    private Object firstEvent;
    @JsonProperty("lastCommit")
    private Object lastCommit;
    @JsonProperty("lastDeploy")
    private Object lastDeploy;
    @JsonProperty("lastEvent")
    private Object lastEvent;
    @JsonProperty("newGroups")
    private Integer newGroups;
    @JsonProperty("owner")
    private Object owner;
    @JsonProperty("projects")
    private List<SentryProject> projects = null;
    @JsonProperty("ref")
    private String ref;
    @JsonProperty("shortVersion")
    private String shortVersion;
    @JsonProperty("url")
    private Object url;
    @JsonProperty("version")
    private String version;

    @JsonProperty("commitCount")
    public Integer getCommitCount() {
        return commitCount;
    }

    @JsonProperty("commitCount")
    public void setCommitCount(Integer commitCount) {
        this.commitCount = commitCount;
    }

    @JsonProperty("dateCreated")
    public String getDateCreated() {
        return dateCreated;
    }

    @JsonProperty("dateCreated")
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @JsonProperty("dateReleased")
    public Object getDateReleased() {
        return dateReleased;
    }

    @JsonProperty("dateReleased")
    public void setDateReleased(Object dateReleased) {
        this.dateReleased = dateReleased;
    }

    @JsonProperty("deployCount")
    public Integer getDeployCount() {
        return deployCount;
    }

    @JsonProperty("deployCount")
    public void setDeployCount(Integer deployCount) {
        this.deployCount = deployCount;
    }

    @JsonProperty("firstEvent")
    public Object getFirstEvent() {
        return firstEvent;
    }

    @JsonProperty("firstEvent")
    public void setFirstEvent(Object firstEvent) {
        this.firstEvent = firstEvent;
    }

    @JsonProperty("lastCommit")
    public Object getLastCommit() {
        return lastCommit;
    }

    @JsonProperty("lastCommit")
    public void setLastCommit(Object lastCommit) {
        this.lastCommit = lastCommit;
    }

    @JsonProperty("lastDeploy")
    public Object getLastDeploy() {
        return lastDeploy;
    }

    @JsonProperty("lastDeploy")
    public void setLastDeploy(Object lastDeploy) {
        this.lastDeploy = lastDeploy;
    }

    @JsonProperty("lastEvent")
    public Object getLastEvent() {
        return lastEvent;
    }

    @JsonProperty("lastEvent")
    public void setLastEvent(Object lastEvent) {
        this.lastEvent = lastEvent;
    }

    @JsonProperty("newGroups")
    public Integer getNewGroups() {
        return newGroups;
    }

    @JsonProperty("newGroups")
    public void setNewGroups(Integer newGroups) {
        this.newGroups = newGroups;
    }

    @JsonProperty("owner")
    public Object getOwner() {
        return owner;
    }

    @JsonProperty("owner")
    public void setOwner(Object owner) {
        this.owner = owner;
    }

    @JsonProperty("projects")
    public List<SentryProject> getProjects() {
        return projects;
    }

    @JsonProperty("projects")
    public void setProjects(List<SentryProject> sentryProjects) {
        this.projects = sentryProjects;
    }

    @JsonProperty("ref")
    public String getRef() {
        return ref;
    }

    @JsonProperty("ref")
    public void setRef(String ref) {
        this.ref = ref;
    }

    @JsonProperty("shortVersion")
    public String getShortVersion() {
        return shortVersion;
    }

    @JsonProperty("shortVersion")
    public void setShortVersion(String shortVersion) {
        this.shortVersion = shortVersion;
    }

    @JsonProperty("url")
    public Object getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(Object url) {
        this.url = url;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

}

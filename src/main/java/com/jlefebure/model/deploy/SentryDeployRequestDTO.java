package com.jlefebure.model.deploy;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SentryDeployRequestDTO {
    @JsonProperty("environment")
    private String environment;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

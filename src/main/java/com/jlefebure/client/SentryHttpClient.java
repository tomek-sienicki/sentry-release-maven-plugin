package com.jlefebure.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlefebure.model.deploy.SentryDeployRequestDTO;
import com.jlefebure.model.release.SentryReleaseRequestDTO;
import com.jlefebure.model.release.SentryReleaseResponseDTO;
import com.squareup.okhttp.*;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;

public class SentryHttpClient {
    private static final String CONTENT_TYPE = "application/json";
    private final Log logger;

    private String baseUrl;
    private String organization;
    private String authToken;
    private ObjectMapper objectMapper;
    private OkHttpClient httpClient;

    public SentryHttpClient(String baseUrl, String authToken, String organization, Log log) {
        this.baseUrl = baseUrl;
        this.organization = organization;
        this.authToken = authToken;
        this.objectMapper = new ObjectMapper();
        this.httpClient = new OkHttpClient();
        this.logger = log;
    }

    public SentryReleaseResponseDTO createRelease(SentryReleaseRequestDTO releaseDTO) throws IOException {
        String uri = "organizations/" + organization +"/releases/";

        Request.Builder builder = new Request.Builder();
        String s = objectMapper.writeValueAsString(releaseDTO);
        Request request = builder
                .url(baseUrl + "/" + uri)
                .addHeader("Content-Type", CONTENT_TYPE)
                .addHeader("Authorization", "Bearer " + authToken)
                .post(RequestBody.create(MediaType.parse(CONTENT_TYPE), s))
                .build();

        logger.info("Send sentry release creation request on url " + request.urlString());
        Response execute = httpClient.newCall(request).execute();
        logger.info("Receive response " + execute.code());
        String body = execute.body().string();
        logger.info(body);

        if (execute.isSuccessful()) {
            return objectMapper.readValue(body, SentryReleaseResponseDTO.class);
        } else {
            return null;
        }
    }

    public boolean createDeploy(SentryDeployRequestDTO deploy, String version) throws IOException {
        String uri = "organizations/" + organization +"/releases/" + version + "/deploys/";

        Request.Builder builder = new Request.Builder();
        String s = objectMapper.writeValueAsString(deploy);
        Request request = builder
                .url(baseUrl + "/" + uri)
                .addHeader("Content-Type", CONTENT_TYPE)
                .addHeader("Authorization", "Bearer " + authToken)
                .post(RequestBody.create(MediaType.parse(CONTENT_TYPE), s))
                .build();

        logger.info("Send sentry release deploy creation request on url " + request.urlString());
        Response execute = httpClient.newCall(request).execute();
        logger.info("Receive response " + execute.code());
        String body = execute.body().string();
        logger.info(body);

        return execute.isSuccessful();
    }

    public SentryReleaseResponseDTO updateRelease(SentryReleaseRequestDTO releaseDTO) throws IOException {
        String uri = "organizations/" + organization +"/releases/";

        Request.Builder builder = new Request.Builder();
        String s = objectMapper.writeValueAsString(releaseDTO);
        Request request = builder
                .url(baseUrl + "/" + uri)
                .addHeader("Content-Type", CONTENT_TYPE)
                .addHeader("Authorization", "Bearer " + authToken)
                .post(RequestBody.create(MediaType.parse(CONTENT_TYPE), s))
                .build();

        logger.info("Send sentry update release request on url " + request.urlString());
        Response execute = httpClient.newCall(request).execute();
        logger.info("Receive response " + execute.code());
        String body = execute.body().string();
        logger.info(body);

        if (execute.isSuccessful()) {
            return objectMapper.readValue(body, SentryReleaseResponseDTO.class);
        } else {
            return null;
        }
    }


    public SentryReleaseResponseDTO getRelease(String version) throws IOException {
        String uri = "organizations/" + organization +"/releases/" + version;
        Request.Builder builder = new Request.Builder();

        Request request = builder
                .url(baseUrl + "/" + uri)
                .addHeader("Content-Type", CONTENT_TYPE)
                .addHeader("Authorization", "Bearer " + authToken)
                .get()
                .build();

        logger.info("Send get release  request on url " + request.urlString());
        Response execute = httpClient.newCall(request).execute();
        logger.info("Receive response " + execute.code());
        String body = execute.body().string();
        logger.info(body);

        if (execute.isSuccessful()) {
            return objectMapper.readValue(body, SentryReleaseResponseDTO.class);
        } else {
            return null;
        }
    }

}

package ru.ilezzov.pluginBlank.manager;

import lombok.Getter;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Getter
public class VersionManager {
    private final String localPluginVersion;
    private final String currentPluginVersion;
    private final String urlToFileVersion;

    public VersionManager(final String localPluginVersion, final String urlToFileVersion) throws URISyntaxException, IOException, InterruptedException {
        this.localPluginVersion = localPluginVersion;
        this.urlToFileVersion = urlToFileVersion;
        this.currentPluginVersion = getCurrentPluginVersionFromGitHub();
    }

    public boolean check() throws URISyntaxException, IOException, InterruptedException {
        return localPluginVersion.equalsIgnoreCase(this.currentPluginVersion);
    }

    private String getCurrentPluginVersionFromGitHub() throws URISyntaxException, IOException, InterruptedException {
        final HttpClient httpClient = HttpClient.newHttpClient();

        assert this.urlToFileVersion != null;

        final HttpRequest httpRequest = HttpRequest.newBuilder(new URI(this.urlToFileVersion))
                .GET()
                .build();

        final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        httpClient.close();

        return httpResponse.body();
    }

}

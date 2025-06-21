package ru.ilezzov.coollobby.managers;

import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
        return equalsVersion(currentPluginVersion, this.localPluginVersion) != 1;
    }

    private String getCurrentPluginVersionFromGitHub() throws URISyntaxException, IOException, InterruptedException {
        final HttpClient httpClient = HttpClient.newHttpClient();
        assert this.urlToFileVersion != null;

        final HttpRequest httpRequest = HttpRequest.newBuilder(new URI(this.urlToFileVersion))
                .GET()
                .build();

        final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return httpResponse.body();
    }

    private int equalsVersion(final String version1, final String version2) {
        final String[] version1Split = version1.split("\\.");
        final String[] version2Split = version2.split("\\.");

        final int maxLength = Math.max(version1Split.length, version2Split.length);

        try {
            for (int i = 0; i < maxLength; i++) {
                final int num1 = i < version1Split.length ? Integer.parseInt(version1Split[i]) : 0;
                final int num2 = i < version2Split.length ? Integer.parseInt(version2Split[i]) : 0;

                if (num1 < num2) {
                    return -1; // version1 < version2
                }

                if (num1 > num2) {
                    return 1; // version1 > version2
                }
            }

            return 0; // version1 == version2
        } catch (NumberFormatException e) {
            return -2;
        }
    }

}

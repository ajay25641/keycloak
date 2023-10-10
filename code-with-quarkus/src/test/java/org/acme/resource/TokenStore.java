package org.acme.resource;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TokenStore {
    private static String accessToken;
    private static String refreshToken;

    public static String getAccessToken() {
        log.info("inside getAccessToken "+accessToken);
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        log.info("inside setAccessToken "+accessToken);
        TokenStore.accessToken = accessToken;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }

    public static void setRefreshToken(String refreshToken) {
        TokenStore.refreshToken = refreshToken;
    }
}

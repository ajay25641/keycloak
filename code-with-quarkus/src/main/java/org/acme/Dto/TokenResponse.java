package org.acme.Dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class TokenResponse {

    private String access_token;

    private int expires_in;

    private int refresh_expires_in;

    private String refresh_token;

    private String token_type;

    private String session_state;

    private String scope;
}

package org.acme.Dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class LoginDto {
    private String grant_type;
    private String client_id;
    private String username;
    private String password;
    private String scope;
}

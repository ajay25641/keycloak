package org.acme.Model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    private String id;

    @NotNull(message = "Username must not be empty!")
    @Size(min=3,message = "Username must be of atleast 3 character!")
    private String username;

    @NotNull(message = "firstName must not be empty!")
    @Size(min=3,message = "FirstName must be of atleast 3 character!")
    private String firstName;

    @NotNull(message = "LastName must not be empty!")
    @Size(min=3,message = "LastName must be of atleast 3 character!")
    private String lastName;

    @Email
    @NotNull(message = "Email must not be empty!")
    private String email;

    @NotNull(message = "Password must not be empty!")
    @Size(min=5,message = "Password must be atleast 5 character!")
    private String password;

    private String enabled;
    private String emailVerified;

}


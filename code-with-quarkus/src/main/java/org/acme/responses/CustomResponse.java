package org.acme.responses;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ws.rs.core.Response;
import lombok.Getter;


@Getter
@RegisterForReflection
public class CustomResponse {
    private int statusCode;
    private String message;
    private Object errorMessages;
    private Object data;

    public static Builder getBuilder(){
        return new CustomResponse.Builder();
    }
    public Response responseBuilder(){
        return Response.status(this.statusCode).entity(this).build();
    }

    public static class Builder{
        private int statusCode;
        private String message;
        private Object errorMessages;
        private Object data;

        public Builder(){}

        public Builder message(String message){
            this.message=message;
            return this;
        }
        public Builder statusCode(int statusCode){
            this.statusCode=statusCode;
            return this;
        }
        public Builder errorMessages(Object errorMessages){
            this.errorMessages=errorMessages;
            return this;
        }
        public Builder data(Object data){
            this.data=data;
            return this;
        }
        public CustomResponse build(){
            CustomResponse customResponse=new CustomResponse();

            customResponse.message=this.message;
            customResponse.errorMessages=this.errorMessages;
            customResponse.statusCode=this.statusCode;
            customResponse.data=this.data;

            return customResponse;
        }
        public Response responseBuilder(){
            return this.build().responseBuilder();
        }
    }

}


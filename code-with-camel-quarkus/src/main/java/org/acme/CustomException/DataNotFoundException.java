package org.acme.CustomException;

import lombok.Getter;

@Getter
public class DataNotFoundException extends RuntimeException{
    private String customMessage;

    public DataNotFoundException(String customMessage,String defaultMessage){
        super(defaultMessage);
        this.customMessage=customMessage;
    }

}

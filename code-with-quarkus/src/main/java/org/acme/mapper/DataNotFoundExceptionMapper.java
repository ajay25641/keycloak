package org.acme.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.acme.Constant.StatusCode;
import org.acme.CustomException.DataNotFoundException;
import org.acme.responses.CustomResponse;

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {
    @Override
    public Response toResponse(DataNotFoundException ex) {
        String message="not found!";

        return CustomResponse
                .getBuilder()
                .message(ex.getCustomMessage()+" "+message)
                .errorMessages(ex.getMessage())
                .statusCode(StatusCode.NOT_FOUND)
                .responseBuilder();
    }
}

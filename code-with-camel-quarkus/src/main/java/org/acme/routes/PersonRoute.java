package org.acme.routes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.acme.Constant.StatusCode;
import org.acme.CustomException.DataNotFoundException;
import org.acme.Interceptor.RouteInterceptor;
import org.acme.Model.Person;
import org.acme.responses.CustomResponse;
import org.acme.service.PersonService;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;


@ApplicationScoped
@Slf4j
public class PersonRoute extends RouteBuilder {

    @Inject
    private PersonService personService;

    @Inject
    private RouteInterceptor routeInterceptor;

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("platform-http")

                .bindingMode(RestBindingMode.json);

        interceptSendToEndpoint("direct:*")
                .to("bean:routeInterceptor");

        rest("/user")
                .post("/createuser")
                     .type(Person.class)
                     .to("direct:createUser")

                .get("/admin/getallusers")
                     .to("direct:getAllUsers")

                .get("/getuserbyid")
                        .param().name("id").type(RestParamType.query).dataType("string").endParam()
                        .to("direct:getUserById")

                .put("/updateuser")
                        .type(Person.class)
                        .to("direct:updateUser")

                .delete("/admin/deleteuser")
                        .param().name("id").type(RestParamType.query).dataType("string").endParam()
                        .to("direct:deleteUser")

                .get("/admin/getuserbyfieldname")
                        .param().name("email").type(RestParamType.query).dataType("string").endParam()
                        .param().name("firstName").type(RestParamType.query).dataType("string").endParam()
                        .param().name("lastName").type(RestParamType.query).dataType("string").endParam()
                        .param().name("username").type(RestParamType.query).dataType("string").endParam()
                        .to("direct:getUserByFieldName");





        from("direct:createUser")
                .routeId("createUserRoute")

                .to("bean:personService?method=createUser")
                .end();

        from("direct:getAllUsers")
                .routeId("getAllUsersRoute")

                .bean(PersonService.class,"getAllUsers")
                .end();

        from("direct:getUserById")
                .routeId("getUserByIdRoute")
                .to("bean:personService?method=getUserById(${header.id})")
                .onException(DataNotFoundException.class)
                .handled(true)
                .to("direct:dataNotFoundException")
                .end();

        from("direct:updateUser")
                .routeId("updateUserRoute")
                .onException(DataNotFoundException.class)
                .handled(true)
                .to("direct:dataNotFoundException")
                .end()
                .choice()
                      .when(simple("${body.id} == null || ${body.id.length()} <= 3"))
                            .setHeader("statusCode", constant(StatusCode.BAD_REQUEST))
                            .setHeader("message",constant("UserId must not be blank!"))
                            .to("direct:throwexception")
                      .otherwise()
                            .to("bean:personService?method=updateUser")
                            .end()
                .end();


        from("direct:deleteUser")
                .routeId("deleteUserRoute")
                .to("bean:personService?method=deleteUser(${header.id})")
                .end();

        from("direct:getUserByFieldName")
                .routeId("getUserByFieldNameRoute")
                .choice()
                        .when(header("email").isNotNull())
                              .setHeader("fieldName",constant("email"))
                        .when(header("firstName").isNotNull())
                              .setHeader("fieldName",constant("firstName"))
                        .when(header("lastName").isNotNull())
                              .setHeader("fieldName",constant("lastName"))
                        .when(header("username").isNotNull())
                              .setHeader("fieldName",constant("username"))
                        .otherwise()
                             .setHeader("statusCode",constant(StatusCode.BAD_REQUEST))
                             .setHeader("message",constant("Please provide the correct field Name!"))
                             .to("direct:throwexception")
                .end()
                .process(exchange -> {
                    Message iMessage=exchange.getIn();

                    String fieldName=iMessage.getHeader("fieldName",String.class);

                    if(fieldName!=null){
                        String fieldValue=iMessage.getHeader(fieldName, String.class);

                        CustomResponse customResponse=personService.getUserByFieldName(fieldValue,fieldName);

                        iMessage.setBody(customResponse);
                    }

                });

        from("direct:dataNotFoundException")
                .routeId("dataNotFoundExceptionRoute")
                .process(exchange -> {
                         String message="not found!";
                         DataNotFoundException ex=exchange.getProperty(Exchange.EXCEPTION_CAUGHT, DataNotFoundException.class);
                         CustomResponse customResponse=CustomResponse
                                 .getBuilder()
                                 .message(ex.getCustomMessage()+" "+message)
                                 .errorMessages(ex.getMessage())
                                 .statusCode(StatusCode.NOT_FOUND).build();

                         exchange.getIn().setBody(customResponse);
                });

        from("direct:throwexception")
                .routeId("throwExceptionRoute")
                .process(exchange->{
                    Message iMessage=exchange.getIn();

                    CustomResponse customResponse=CustomResponse
                            .getBuilder()
                            .statusCode(iMessage.getHeader("statusCode", Integer.class))
                            .message(iMessage.getHeader("message", String.class))
                            .build();
                });

    }

}

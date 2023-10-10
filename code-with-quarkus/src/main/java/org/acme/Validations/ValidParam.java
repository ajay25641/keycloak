package org.acme.Validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Size(min=3, message = "It must be atleast 3 characters long!")
public @interface ValidParam {
    String message() default "Invalid param";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

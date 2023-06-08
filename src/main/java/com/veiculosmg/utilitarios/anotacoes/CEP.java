package com.veiculosmg.utilitarios.anotacoes;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CEP.CepValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CEP {

    String message() default "CEP inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CepValidator implements ConstraintValidator<CEP, String> {

        private static final String PADRAO_CEP = "^[0-9]{8}$";

        @Override
        public boolean isValid(String numeroCep, ConstraintValidatorContext context) {
            return numeroCep != null && numeroCep.matches(PADRAO_CEP);
        }
    }

}

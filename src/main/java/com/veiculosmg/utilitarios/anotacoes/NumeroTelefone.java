package com.veiculosmg.utilitarios.anotacoes;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NumeroTelefone.NumeroTelefoneValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NumeroTelefone {

    String message() default "Número de telefone inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class NumeroTelefoneValidator implements ConstraintValidator<NumeroTelefone, String> {

        private static final String PADRAO_NUMERO_TELEFONE = "^[0-9]{10}$";

        @Override
        public boolean isValid(String numeroTelefone, ConstraintValidatorContext context) {
            return numeroTelefone != null && numeroTelefone.matches(PADRAO_NUMERO_TELEFONE);
        }
    }

}

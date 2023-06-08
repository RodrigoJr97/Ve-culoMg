package com.veiculosmg.utilitarios.anotacoes;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidacaoNumeroTelefone.NumeroTelefoneValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidacaoNumeroTelefone {

    String message() default "Número de telefone inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class NumeroTelefoneValidator implements ConstraintValidator<ValidacaoNumeroTelefone, String> {

        private static final String PADRAO_NUMERO_TELEFONE = "^[0-9]{11}$";

        @Override
        public boolean isValid(String numeroTelefone, ConstraintValidatorContext context) {
            return numeroTelefone != null && numeroTelefone.matches(PADRAO_NUMERO_TELEFONE);
        }
    }

}

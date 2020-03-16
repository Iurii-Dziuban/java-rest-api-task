package de.egym.recruiting.codingtask.rest.validation;

import de.egym.recruiting.codingtask.jpa.domain.Exercise;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {HasId.Validator.class})
public @interface HasId {

    String message() default "Should have id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    public class Validator implements ConstraintValidator<HasId, Exercise> {
        @Override
        public void initialize(final HasId hasId) {
        }
        @Override
        public boolean isValid(final Exercise exercise, final ConstraintValidatorContext constraintValidatorContext) {
            return exercise == null || exercise.getId() != null;
        }
    }
}

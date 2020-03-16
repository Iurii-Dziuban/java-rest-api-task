package de.egym.recruiting.codingtask.rest.validation;

import de.egym.recruiting.codingtask.jpa.domain.Exercise;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {HasNoId.Validator.class})
public @interface HasNoId {

    String message() default "Should not have Id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    public class Validator implements ConstraintValidator<HasNoId, Exercise> {
        @Override
        public void initialize(final HasNoId hasId) {
        }
        @Override
        public boolean isValid(final Exercise exercise, final ConstraintValidatorContext constraintValidatorContext) {
            return exercise == null || exercise.getId() == null;
        }
    }
}

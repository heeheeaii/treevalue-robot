package com.treevalue.robot.resource.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [ListSizeGreaterThanZeroValidator::class])
annotation class ListSizeGreaterThanZero(
    val message: String = "List element length must be greater than zero",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)

private class ListSizeGreaterThanZeroValidator : ConstraintValidator<ListSizeGreaterThanZero, List<String>> {
    override fun isValid(value: List<String>?, context: ConstraintValidatorContext): Boolean {
        return !value.isNullOrEmpty()
    }
}

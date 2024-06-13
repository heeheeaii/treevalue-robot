package com.treevalue.robot.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(value = [IllegalArgumentException::class, NullPointerException::class])
    fun handleIllegalArgumentAndNullPointer(ex: Exception): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.message)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

//    @ExceptionHandler(value = [ResourceNotFoundException::class])
//    fun handleResourceNotFoundException(ex: ResourceNotFoundException): ResponseEntity<ErrorResponse> {
//        val error = ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage())
//        return ResponseEntity(error, HttpStatus.NOT_FOUND)
//    }

    class ErrorResponse // getter and setter methods...
        (private val status: Int, private val message: String?)
}

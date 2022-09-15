package org.bsf.task.controller.handler

import org.apache.logging.log4j.kotlin.Logging
import org.bsf.task.dto.ErrorResponseDto
import org.bsf.task.exception.BsfBusinessException
import org.bsf.task.exception.EntityDeletedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(request: HttpServletRequest, exception: EntityNotFoundException): ErrorResponseDto {
        logger.info(exception.message!!)
        return ErrorResponseDto(exception.message!!)
    }

    @ExceptionHandler(BsfBusinessException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBsfBusinessException(
        request: HttpServletRequest,
        exception: BsfBusinessException
    ): ErrorResponseDto {
        logger.info(exception.message!!)
        return ErrorResponseDto(exception.message)
    }

    @ExceptionHandler(EntityDeletedException::class)
    fun handleEntityDeletedException(
        request: HttpServletRequest,
        exception: EntityDeletedException
    ): ResponseEntity<Any> {
        logger.info(exception.message!!)
        return ResponseEntity.noContent().build()
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(
        request: HttpServletRequest,
        exception: MethodArgumentNotValidException
    ): ErrorResponseDto {
        logger.info(exception.message)
        return ErrorResponseDto(exception.message)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(
        request: HttpServletRequest,
        exception: Exception
    ): ErrorResponseDto {
        logger.error(exception.message!!)
        return ErrorResponseDto(exception.message!!)
    }

    companion object : Logging

}
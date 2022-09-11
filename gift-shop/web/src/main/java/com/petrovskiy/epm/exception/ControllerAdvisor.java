package com.petrovskiy.epm.exception;

import antlr.MismatchedCharException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.net.BindException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static com.petrovskiy.epm.exception.ExceptionCode.*;


@RestControllerAdvice
public class ControllerAdvisor {
    private static final Object[] EMPTY_ARGS = new Object[0];
    private static final String INITIAL_ERROR_MSG = "error_msg.";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String ERROR_CODE = "errorCode";
    private final ResourceBundleMessageSource messages;
    private MismatchedCharException messageSource;

    @Autowired
    public ControllerAdvisor(ResourceBundleMessageSource messages) {
        this.messages = messages;
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<Object> handleSystemException(SystemException e, Locale locale) {
        final int errorCode = e.getErrorCode();
        return new ResponseEntity<>(createResponse(errorCode, locale), getHttpStatusByCode(errorCode));
    }

    @ExceptionHandler({NumberFormatException.class, BindException.class})
    public ResponseEntity<Object> handleNumberFormatException(Locale locale) {
        return new ResponseEntity<>(createResponse(BAD_REQUEST, locale), getHttpStatusByCode(BAD_REQUEST));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,Locale locale) {
        return new ResponseEntity<>(createResponse(BAD_REQUEST, locale), getHttpStatusByCode(BAD_REQUEST));

    }

    /*@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,Locale locale) {
        return new ResponseEntity<>(createResponse(BAD_REQUEST, locale), getHttpStatusByCode(BAD_REQUEST));

    }*/

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentNotValid(MethodArgumentNotValidException ex,Locale locale) {
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String filedName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            errors.put(filedName,message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(Locale locale) {
        return new ResponseEntity<>(createResponse(UNREADABLE_MESSAGE, locale), getHttpStatusByCode(UNREADABLE_MESSAGE));
    }

    @ExceptionHandler(JDBCConnectionException.class)
    public ResponseEntity<Object> handleJDBCConnectionException(Locale locale) {
        return new ResponseEntity<>(createResponse(DATA_BASE_ERROR, locale), getHttpStatusByCode(DATA_BASE_ERROR));
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Object> handlePropertyReferenceException(Locale locale) {
        return new ResponseEntity<>(createResponse(INVALID_ATTRIBUTE_LIST, locale), getHttpStatusByCode(INVALID_ATTRIBUTE_LIST));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(Locale locale) {
        return new ResponseEntity<>(createResponse(FORBIDDEN_ACCESS, locale), getHttpStatusByCode(FORBIDDEN_ACCESS));
    }

    /*@ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(Locale locale) {
        return new ResponseEntity<>(createResponse(INVALID_CREDENTIALS, locale), getHttpStatusByCode(INVALID_CREDENTIALS));
    }*/

    private Map<String, Object> createResponse(int errorCode, Locale locale) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(ERROR_MESSAGE, messages.getMessage(getMessageByCode(errorCode), EMPTY_ARGS, locale));
        response.put(ERROR_CODE, errorCode);
        return response;
    }

    private String getMessageByCode(int errorCode) {
        return INITIAL_ERROR_MSG + errorCode;
    }

    private HttpStatus getHttpStatusByCode(int errorCode) {
        int statusCode = errorCode / 100;
        return HttpStatus.valueOf(statusCode);
    }
}
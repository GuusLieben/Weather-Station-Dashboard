package org.dockbox.climate.rest;

import org.dockbox.climate.exceptions.IllegalDateQuery;
import org.dockbox.climate.model.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalDateQuery.class)
    public ErrorObject handleIllegalQuery(HttpServletRequest req, IllegalDateQuery iqe) {
        return new ErrorObject(
                400,
                "Invalid Query",
                "Invalid date range '" + iqe.getBefore().toString() + " - " + iqe.getAfter().toString() + "'",
                req);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorObject handle(HttpServletRequest req) {
        return new ErrorObject(500, "Unknown Exception", "The server failed to process your request", req);
    }

}

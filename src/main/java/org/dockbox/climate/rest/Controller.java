package org.dockbox.climate.rest;

import org.dockbox.climate.model.ErrorObject;

import javax.servlet.http.HttpServletRequest;

public abstract class Controller {

    protected static Object error(int status, String error, String message, HttpServletRequest request) {
        return new ErrorObject(status, error, message, request);
    }

}

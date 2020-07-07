package org.dockbox.climate.exceptions;

public class IllegalParameterQuery extends RuntimeException {

    private final String parameterName;
    private final String parameterValue;

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public IllegalParameterQuery(String parameterName, String parameterValue) {
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public IllegalParameterQuery(String message, String parameterName, String parameterValue) {
        super(message);
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public IllegalParameterQuery(String message, Throwable cause, String parameterName, String parameterValue) {
        super(message, cause);
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public IllegalParameterQuery(Throwable cause, String parameterName, String parameterValue) {
        super(cause);
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public IllegalParameterQuery(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String parameterName, String parameterValue) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }
}

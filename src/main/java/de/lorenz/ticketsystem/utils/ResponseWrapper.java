package de.lorenz.ticketsystem.utils;

import de.lorenz.ticketsystem.globals.GlobalHttpStatusCode;

public record ResponseWrapper<T>(String message, int statusCode, T response) {

    public static <T> ResponseWrapper<T> ok(T data, String message) {
        return new ResponseWrapper<>(message, GlobalHttpStatusCode.OK.getCode(), data);
    }

    public static <T> ResponseWrapper<T> error(String message) {
        return new ResponseWrapper<>(message, GlobalHttpStatusCode.INTERNAL_ERROR.getCode(), null);
    }

    public static <T> ResponseWrapper<T> error(T data, String message) {
        return new ResponseWrapper<>(message, GlobalHttpStatusCode.INTERNAL_ERROR.getCode(), data);
    }

    public static <T> ResponseWrapper<T> badRequest(String message) {
        return new ResponseWrapper<>(message, GlobalHttpStatusCode.BAD_REQUEST.getCode(), null);
    }

    public static <T> ResponseWrapper<T> badRequest(T data, String message) {
        return new ResponseWrapper<>(message, GlobalHttpStatusCode.BAD_REQUEST.getCode(), data);
    }

    public static <T> ResponseWrapper<T> unauthorized(String message) {
        return new ResponseWrapper<>(message, GlobalHttpStatusCode.UNAUTHORIZED.getCode(), null);
    }

    public static <T> ResponseWrapper<T> unauthorized(T data, String message) {
        return new ResponseWrapper<>(message, GlobalHttpStatusCode.UNAUTHORIZED.getCode(), data);
    }

    public static <T> ResponseWrapper<T> forbidden(String message) {
        return new ResponseWrapper<>(message, GlobalHttpStatusCode.FORBIDDEN.getCode(), null);
    }

    public static <T> ResponseWrapper<T> forbidden(T data, String message) {
        return new ResponseWrapper<>(message, GlobalHttpStatusCode.FORBIDDEN.getCode(), data);
    }

    public static <T> ResponseWrapper<T> notFound(String message) {
        return new ResponseWrapper<>(message, GlobalHttpStatusCode.NOT_FOUND.getCode(), null);
    }

    public static <T> ResponseWrapper<T> notFound(T data, String message) {
        return new ResponseWrapper<>(message, GlobalHttpStatusCode.NOT_FOUND.getCode(), data);
    }

}

package de.lorenz.ticketsystem.utils;

import de.lorenz.ticketsystem.globals.GlobalHttpStatusCode;

public record ResponseWrapper<T>(String message, T response, int statusCode) {

    public static <T> ResponseWrapper<T> ok(T data, String message) {
        return new ResponseWrapper<>(message, data, GlobalHttpStatusCode.OK.getCode());
    }

    public static <T> ResponseWrapper<T> error(String message) {
        return new ResponseWrapper<>(message, null, GlobalHttpStatusCode.INTERNAL_ERROR.getCode());
    }

    public static <T> ResponseWrapper<T> error(T data, String message) {
        return new ResponseWrapper<>(message, data, GlobalHttpStatusCode.INTERNAL_ERROR.getCode());
    }

    public static <T> ResponseWrapper<T> badRequest(String message) {
        return new ResponseWrapper<>(message, null, GlobalHttpStatusCode.BAD_REQUEST.getCode());
    }

    public static <T> ResponseWrapper<T> badRequest(T data, String message) {
        return new ResponseWrapper<>(message, data, GlobalHttpStatusCode.BAD_REQUEST.getCode());
    }

    public static <T> ResponseWrapper<T> unauthorized(String message) {
        return new ResponseWrapper<>(message, null, GlobalHttpStatusCode.UNAUTHORIZED.getCode());
    }

    public static <T> ResponseWrapper<T> unauthorized(T data, String message) {
        return new ResponseWrapper<>(message, data, GlobalHttpStatusCode.UNAUTHORIZED.getCode());
    }

    public static <T> ResponseWrapper<T> forbidden(String message) {
        return new ResponseWrapper<>(message, null, GlobalHttpStatusCode.FORBIDDEN.getCode());
    }

    public static <T> ResponseWrapper<T> forbidden(T data, String message) {
        return new ResponseWrapper<>(message, data, GlobalHttpStatusCode.FORBIDDEN.getCode());
    }

    public static <T> ResponseWrapper<T> notFound(String message) {
        return new ResponseWrapper<>(message, null, GlobalHttpStatusCode.NOT_FOUND.getCode());
    }

    public static <T> ResponseWrapper<T> notFound(T data, String message) {
        return new ResponseWrapper<>(message, data, GlobalHttpStatusCode.NOT_FOUND.getCode());
    }

}

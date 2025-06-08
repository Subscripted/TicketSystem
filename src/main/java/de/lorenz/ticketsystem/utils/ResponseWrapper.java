package de.lorenz.ticketsystem.utils;

import de.lorenz.ticketsystem.globals.GlobalHttpStatusCode;

public record ResponseWrapper<T>(T response, String message, int statusCode) {

    public static <T> ResponseWrapper<T> ok(T data) {
        return new ResponseWrapper<>(data, "OK", GlobalHttpStatusCode.OK.getCode());
    }

    public static <T> ResponseWrapper<T> ok(T data, String message) {
        return new ResponseWrapper<>(data, message, GlobalHttpStatusCode.OK.getCode());
    }

    public static <T> ResponseWrapper<T> error(String message) {
        return new ResponseWrapper<>(null, message, GlobalHttpStatusCode.INTERNAL_ERROR.getCode());
    }

    public static <T> ResponseWrapper<T> error(T data, String message) {
        return new ResponseWrapper<>(data, message, GlobalHttpStatusCode.INTERNAL_ERROR.getCode());
    }

    public static <T> ResponseWrapper<T> badRequest(String message) {
        return new ResponseWrapper<>(null, message, GlobalHttpStatusCode.BAD_REQUEST.getCode());
    }

    public static <T> ResponseWrapper<T> badRequest(T data, String message) {
        return new ResponseWrapper<>(data, message, GlobalHttpStatusCode.BAD_REQUEST.getCode());
    }

    public static <T> ResponseWrapper<T> unauthorized(String message) {
        return new ResponseWrapper<>(null, message, GlobalHttpStatusCode.UNAUTHORIZED.getCode());
    }

    public static <T> ResponseWrapper<T> unauthorized(T data, String message) {
        return new ResponseWrapper<>(data, message, GlobalHttpStatusCode.UNAUTHORIZED.getCode());
    }

    public static <T> ResponseWrapper<T> forbidden(String message) {
        return new ResponseWrapper<>(null, message, GlobalHttpStatusCode.FORBIDDEN.getCode());
    }

    public static <T> ResponseWrapper<T> forbidden(T data, String message) {
        return new ResponseWrapper<>(data, message, GlobalHttpStatusCode.FORBIDDEN.getCode());
    }

    public static <T> ResponseWrapper<T> notFound(String message) {
        return new ResponseWrapper<>(null, message, GlobalHttpStatusCode.NOT_FOUND.getCode());
    }

    public static <T> ResponseWrapper<T> notFound(T data, String message) {
        return new ResponseWrapper<>(data, message, GlobalHttpStatusCode.NOT_FOUND.getCode());
    }

}

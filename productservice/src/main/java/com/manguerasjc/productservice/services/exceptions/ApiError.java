package com.manguerasjc.productservice.services.exceptions;

public record ApiError(
        int status,
        String error,
        String message,
        String path
) {
}

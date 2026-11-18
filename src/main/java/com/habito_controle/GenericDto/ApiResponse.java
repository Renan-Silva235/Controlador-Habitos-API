package com.habito_controle.GenericDto;

public record ApiResponse<T>(
        boolean success,
        String msg,
        T data
) {
    public static <T> ApiResponse<T> success(String msg){
        return new ApiResponse<>(true, msg, null);
    }

    public static <T> ApiResponse<T> success(String msg, T data){
        return new ApiResponse<>(true, msg, data);
    }
}

package com.example.library.web.converter;

import com.example.library.web.dto.ApiResponseDto;
import com.example.library.web.dto.enums.ApiError;
import org.springframework.http.ResponseEntity;

public class ApiConverter {

    /**
     * Creates ResponseEntity containing ApiError with generic body.
     *
     * @param apiError ApiError to be resolved.
     * @param <T>      Generic body to be included in ApiError.
     * @return ResponseEntity containing ApiError with required body.
     */
    public static <T> ResponseEntity<ApiResponseDto<T>> resolveApiError(ApiError apiError) {
        ApiResponseDto<T> dtoWrapper = ApiResponseDto.<T>builder()
                .httpError(apiError.getHttpStatus().value())
                .apiError(apiError.getApiError())
                .apiErrorMsg(apiError.getApiErrorMsg()).build();
        return new ResponseEntity<>(dtoWrapper, apiError.getHttpStatus());
    }
}

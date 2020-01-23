package com.example.library.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ApiResponseDto<T> {

    private Integer httpError;
    private Integer apiError;
    private String apiErrorMsg;
    private T content;
}

package com.jeet.schedular.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Filter {
    @NotBlank(message = "Keyword is mandatory")
    private String keyword;
    @Min(value = 0, message = "Minimum page must be greater than or equal to 0")
    private int page;
    @Min(value = 1, message = "Minimum size must be greater than 0")
    private int size;
}

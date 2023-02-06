package hu.ponte.hr.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
    private String errorCode;
    private String error;
    private String details;
}
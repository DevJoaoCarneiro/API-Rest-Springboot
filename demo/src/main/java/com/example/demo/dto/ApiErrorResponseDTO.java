package com.example.demo.dto;

import java.time.LocalDateTime;

public record ApiErrorResponseDTO(LocalDateTime timestamp,
                                  Integer status,
                                  String error,
                                  String message,
                                  String path) {
}

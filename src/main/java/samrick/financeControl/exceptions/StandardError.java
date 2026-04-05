package samrick.financeControl.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record StandardError(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:MM:ss")
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
}

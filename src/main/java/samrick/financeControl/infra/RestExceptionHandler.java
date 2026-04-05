package samrick.financeControl.infra;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import samrick.financeControl.exceptions.StandardError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // 1. Captura erros de regra de negócio (ex: Usuário não encontrado)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardError> runtimeHandler(RuntimeException exception, HttpServletRequest request) {
        StandardError err = new StandardError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Regra de Negócio",
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    // 2. Captura erros de validação (@NotBlank, @Positive, @Size)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<StandardError> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            StandardError err = new StandardError(
                    LocalDateTime.now(),
                    status.value(),
                    "Erro de Validação: " + fieldError.getField(),
                    fieldError.getDefaultMessage(),
                    request.getContextPath()
            );
            errors.add(err);
        });

        return ResponseEntity.status(status).body(errors);
    }

    // 3. Captura erros de sintaxe (Enum inválido, JSON quebrado)
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        StandardError err = new StandardError(
                LocalDateTime.now(),
                status.value(),
                "Erro de Sintaxe no JSON",
                "Dados inválidos. Verifique o campo 'tipo' (RECEITA/DESPESA) ou o formato da data.",
                request.getContextPath()
        );

        return ResponseEntity.status(status).body(err);
    }
}
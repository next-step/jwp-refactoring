package kitchenpos.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({IllegalArgumentException.class, NotExistEntityException.class})
    public ResponseEntity badRequestExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}

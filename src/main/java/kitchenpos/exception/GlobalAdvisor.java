package kitchenpos.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalAdvisor {

    @ExceptionHandler({ IllegalArgumentException.class, EntityNotFoundException.class })
    public ResponseEntity<Void> handleException() {
        return ResponseEntity.badRequest().build();
    }
}
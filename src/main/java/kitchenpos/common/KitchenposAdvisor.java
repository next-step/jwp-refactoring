package kitchenpos.common;

import kitchenpos.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class KitchenposAdvisor {

    @ExceptionHandler({ IllegalArgumentException.class, EntityNotFoundException.class })
    public ResponseEntity<Void> handleException() {
        return ResponseEntity.badRequest().build();
    }
}

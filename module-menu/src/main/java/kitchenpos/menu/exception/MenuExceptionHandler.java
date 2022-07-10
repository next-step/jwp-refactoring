package kitchenpos.menu.exception;

import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MenuExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class, IllegalStateException.class})
    public ResponseEntity<Void> badRequest(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }

}

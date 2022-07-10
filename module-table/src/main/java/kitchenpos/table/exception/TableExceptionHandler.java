package kitchenpos.table.exception;

import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TableExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class, IllegalStateException.class})
    public ResponseEntity<Void> badRequest(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }

}

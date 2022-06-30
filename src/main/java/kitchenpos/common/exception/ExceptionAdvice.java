package kitchenpos.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(KitchenPosException.class)
    public ResponseEntity handleException(KitchenPosException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
}

package kitchenpos.common;

import kitchenpos.exception.InvalidGuestNumberException;
import kitchenpos.exception.InvalidMenuNumberException;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.exception.InvalidQuantityException;
import kitchenpos.exception.InvalidTableNumberException;
import kitchenpos.exception.NotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(NotExistException.class)
    protected ResponseEntity handleNotExistException(NotExistException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler({InvalidGuestNumberException.class, InvalidMenuNumberException.class,
            InvalidTableNumberException.class, InvalidOrderStatusException.class, InvalidOrderStatusException.class,
            InvalidPriceException.class, InvalidQuantityException.class})
    protected ResponseEntity handleInvalidNUmberException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}

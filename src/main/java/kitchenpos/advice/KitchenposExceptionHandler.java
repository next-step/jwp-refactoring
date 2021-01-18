package kitchenpos.advice;


import kitchenpos.advice.exception.MenuException;
import kitchenpos.advice.exception.MenuGroupException;
import kitchenpos.advice.exception.ProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class KitchenposExceptionHandler {

    @ExceptionHandler(MenuGroupException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity check(MenuGroupException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(ProductException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity check(ProductException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

    }

    @ExceptionHandler(MenuException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity check(MenuException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

    }
}

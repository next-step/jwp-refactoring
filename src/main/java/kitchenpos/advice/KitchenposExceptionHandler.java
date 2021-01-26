package kitchenpos.advice;


import kitchenpos.advice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class KitchenposExceptionHandler {

    @ExceptionHandler({MenuGroupException.class, ProductException.class, PriceException.class,
            MenuException.class, TableGroupException.class, OrderTableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity check(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}

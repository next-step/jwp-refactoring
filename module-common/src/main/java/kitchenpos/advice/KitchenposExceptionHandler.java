package kitchenpos.advice;

import kitchenpos.advice.exception.MenuException;
import kitchenpos.advice.exception.MenuGroupException;
import kitchenpos.advice.exception.OrderException;
import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.advice.exception.PriceException;
import kitchenpos.advice.exception.ProductException;
import kitchenpos.advice.exception.TableGroupException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class KitchenposExceptionHandler {

    @ExceptionHandler({MenuGroupException.class, ProductException.class, PriceException.class, OrderException.class,
            MenuException.class, TableGroupException.class, OrderTableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity check(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}

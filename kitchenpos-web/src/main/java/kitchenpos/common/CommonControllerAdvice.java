package kitchenpos.common;

import kitchenpos.common.exception.InvalidPriceException;
import kitchenpos.common.exception.InvalidQuantityException;
import kitchenpos.menu.exception.NotExistMenuException;
import kitchenpos.menu.exception.NotExistMenuGroupException;
import kitchenpos.order.exception.NotExistOrderException;
import kitchenpos.order.exception.UnableChangeEmptyStatusException;
import kitchenpos.order.exception.UnableChangeOrderStatusException;
import kitchenpos.order.exception.UnableCreateOrderException;
import kitchenpos.product.exception.NotExistProductException;
import kitchenpos.table.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class CommonControllerAdvice {

    @ExceptionHandler({InvalidPriceException.class, InvalidQuantityException.class,
            NotExistMenuException.class, NotExistMenuGroupException.class,
            NotExistOrderException.class, UnableChangeEmptyStatusException.class,
            UnableChangeOrderStatusException.class, UnableCreateOrderException.class,
            NotExistProductException.class, InvalidNumberOfGeustsException.class,
            NotExistOrderTableException.class, NotExistTableException.class,
            UnableChangeEmptyOrderTableException.class, UnableChangeNumberOfGuestsException.class,
            UnableCreateTableGroupException.class, UnableOrderCausedByEmptyTableException.class,
            UnableUngroupStatusException.class
    })
    public ResponseEntity handleIllegalArgsException(Exception e) {
        return ResponseEntity.badRequest().build();
    }
}

package kitchenpos.advice.exception;

import java.math.BigDecimal;

public class MenuException extends RuntimeException {

    public MenuException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}

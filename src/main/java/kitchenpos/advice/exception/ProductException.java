package kitchenpos.advice.exception;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ProductException extends RuntimeException {

    public ProductException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }

    public ProductException(String message, BigDecimal price) {
        super(String.format(message + "price : %d", price));
    }
}

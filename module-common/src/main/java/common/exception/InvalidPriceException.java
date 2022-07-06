package common.exception;

import common.domain.Price;

public class InvalidPriceException extends RuntimeException{

    private static final String MESSAGE_FORMAT = "invalid price. price:%s";

    public InvalidPriceException(Price price) {
        super(String.format(MESSAGE_FORMAT, price));
    }
}

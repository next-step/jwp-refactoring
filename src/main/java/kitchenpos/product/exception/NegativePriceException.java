package kitchenpos.product.exception;

public class NegativePriceException extends RuntimeException {
    public NegativePriceException(String messageNegativePrice) {
        super(messageNegativePrice);
    }
}

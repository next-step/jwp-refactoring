package kitchenpos.exception;

public class OrderException extends CustomException {

    private static final String CLASSIFICATION = "ORDER";

    public OrderException(String message) {
        super(CLASSIFICATION, message);
    }
}

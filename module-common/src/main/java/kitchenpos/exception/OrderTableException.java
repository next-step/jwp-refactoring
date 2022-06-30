package kitchenpos.exception;

public class OrderTableException extends CustomException {

    private static final String CLASSIFICATION = "ORDERTABLE";

    public OrderTableException(String message) {
        super(CLASSIFICATION, message);
    }
}

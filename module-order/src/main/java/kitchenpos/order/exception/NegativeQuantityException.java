package kitchenpos.order.exception;

public class NegativeQuantityException extends RuntimeException{
    public static final String INVALID_QUANTITY = "수량(Quantity)는 0보다 작을 수 없습니다.";

    public NegativeQuantityException(long quantity) {
        super(String.format(INVALID_QUANTITY, quantity));
    }
}

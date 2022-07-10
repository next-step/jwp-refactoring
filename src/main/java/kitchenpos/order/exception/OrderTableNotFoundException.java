package kitchenpos.order.exception;

public class OrderTableNotFoundException extends RuntimeException {
    private static final String message = "주문 테이블 정보가 존재하지 않습니다.";

    public OrderTableNotFoundException() {
        super(message);
    }
}

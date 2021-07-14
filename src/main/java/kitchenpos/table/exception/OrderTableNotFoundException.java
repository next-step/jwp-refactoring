package kitchenpos.table.exception;

public class OrderTableNotFoundException extends RuntimeException {
    public OrderTableNotFoundException() {
        super("대상 주문테이블이 존재하지 않습니다.");
    }

    public OrderTableNotFoundException(String message) {
        super(message);
    }
}

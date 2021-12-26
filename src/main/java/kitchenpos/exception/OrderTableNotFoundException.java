package kitchenpos.exception;

public class OrderTableNotFoundException extends RuntimeException {
    public static final String ORDER_TABLE_NOT_FOUND = "주문테이블을 찾을 수 없습니다.";

    public OrderTableNotFoundException() {
        super(ORDER_TABLE_NOT_FOUND);
    }
}

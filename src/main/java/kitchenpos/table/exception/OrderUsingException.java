package kitchenpos.table.exception;

public class OrderUsingException extends IllegalArgumentException {
    private static final long serialVersionUID = 540236956800849912L;
    private static final String ORDER_USING = "테이블에 속한 주문이 조리중이거나, 식사 중이라 테이블을 업데이트 할 수 없습니다.";

    public OrderUsingException() {
        super(ORDER_USING);
    }

    public OrderUsingException(String message) {
        super(message);
    }
}

package kitchenpos.common.exception;

public class OrderLineItemEmptyException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "주문 항목이 없으면 주문을 등록 할 수 없습니다.";

    public OrderLineItemEmptyException() {
        super(EXCEPTION_MESSAGE);
    }
}

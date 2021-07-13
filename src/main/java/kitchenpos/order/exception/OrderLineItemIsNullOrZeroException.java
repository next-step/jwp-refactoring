package kitchenpos.order.exception;

public class OrderLineItemIsNullOrZeroException extends IllegalArgumentException {

    public OrderLineItemIsNullOrZeroException() {
        super("주문라인아이템은 1개 이상 선택해주세요");
    }

}

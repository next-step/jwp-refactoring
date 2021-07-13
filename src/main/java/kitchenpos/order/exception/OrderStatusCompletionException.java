package kitchenpos.order.exception;

public class OrderStatusCompletionException extends IllegalArgumentException {

    public OrderStatusCompletionException() {
        super("완료된 주문의 상태는 변경 할 수 없습니다.");
    }

}

package kitchenpos.order.exception;

public class OrderStatusCompleteException extends RuntimeException{
    public OrderStatusCompleteException() {
        super("주문완료인 주문상태를 변경하려고 합니다");
    }
}

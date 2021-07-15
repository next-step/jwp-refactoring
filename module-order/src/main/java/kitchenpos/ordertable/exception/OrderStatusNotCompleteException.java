package kitchenpos.ordertable.exception;

public class OrderStatusNotCompleteException extends RuntimeException {
    public OrderStatusNotCompleteException() {
        super("주문 상태값이 결제 완료가 아닙니다");
    }
}

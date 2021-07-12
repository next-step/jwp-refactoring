package kitchenpos.order.exception;

public class NotFoundOrder extends RuntimeException {
    public NotFoundOrder() {
        super("존재하지 않은 주문 입니다.");
    }
}

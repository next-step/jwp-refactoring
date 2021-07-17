package kitchenpos.order.exception;

public class NotEqualsOrderCountAndMenuCount extends RuntimeException {
    public NotEqualsOrderCountAndMenuCount() {
        super("주문한 메뉴의 수가 실제 메뉴 수와 다릅니다.");
    }
}

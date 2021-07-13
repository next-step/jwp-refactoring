package kitchenpos.order.exception;

public class NotFoundOrderTable extends RuntimeException {
    public NotFoundOrderTable() {
        super("테이블이 존재하지 않습니다.");
    }
}

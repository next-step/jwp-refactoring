package kitchenpos.table.exception;

public class NotFoundOrderTableException extends IllegalArgumentException {

    public NotFoundOrderTableException() {
        super("주문테이블을 찾을 수 없습니다.");
    }

}

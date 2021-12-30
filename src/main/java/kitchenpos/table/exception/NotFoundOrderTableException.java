package kitchenpos.table.exception;

public class NotFoundOrderTableException extends RuntimeException {
    public NotFoundOrderTableException(Long id) {
        super(id + " 주문테이블이 존재하지 않습니다");
    }
}

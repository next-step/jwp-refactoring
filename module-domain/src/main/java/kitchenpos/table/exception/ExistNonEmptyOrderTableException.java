package kitchenpos.table.exception;

public class ExistNonEmptyOrderTableException extends RuntimeException {

    public ExistNonEmptyOrderTableException() {
        super("단체 지정될 주문테이블들은 모두 빈 테이블이어야 합니다.");
    }
}

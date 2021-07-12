package kitchenpos.table.exception;

public class NotExistOrderTable extends RuntimeException {
    public NotExistOrderTable() {
        super("존재하지 않는 주문 테이블입니다.");
    }
}

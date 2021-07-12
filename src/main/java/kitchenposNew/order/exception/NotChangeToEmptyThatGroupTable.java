package kitchenposNew.order.exception;

public class NotChangeToEmptyThatGroupTable extends RuntimeException {
    public NotChangeToEmptyThatGroupTable() {
        super("단체 테이블은 빈 상태로 처리할 수 없습니다.");
    }
}

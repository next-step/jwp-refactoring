package kitchenposNew.table.exception;

public class NotChangeNumberOfGuestThatEmptyTable extends RuntimeException {
    public NotChangeNumberOfGuestThatEmptyTable() {
        super("빈 테이블은 손님 수를 변경할 수 없습니다.");
    }
}

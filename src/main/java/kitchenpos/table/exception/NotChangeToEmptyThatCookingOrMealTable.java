package kitchenpos.table.exception;

public class NotChangeToEmptyThatCookingOrMealTable extends RuntimeException {
    public NotChangeToEmptyThatCookingOrMealTable() {
        super("식사 중 이거나 요리 중인 테이블은 빈 테이블로 변경할 수 없습니다.");
    }
}

package kitchenpos.table.exception;

public class NotFoundTableGroup extends RuntimeException {
    public NotFoundTableGroup() {
        super("존재하지 않는 단체 테이블입니다.");
    }
}

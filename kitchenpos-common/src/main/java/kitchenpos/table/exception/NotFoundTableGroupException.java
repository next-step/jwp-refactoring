package kitchenpos.table.exception;

public class NotFoundTableGroupException extends RuntimeException {
    public NotFoundTableGroupException() {
        super("존재하지 않는 단체 테이블입니다.");
    }
}

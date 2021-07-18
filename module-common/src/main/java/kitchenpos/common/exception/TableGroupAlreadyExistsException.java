package kitchenpos.common.exception;

public class TableGroupAlreadyExistsException extends RuntimeException {
    public TableGroupAlreadyExistsException() {
        super("테이블 그룹이 이미 존재합니다.");
    }

    public TableGroupAlreadyExistsException(String message) {
        super(message);
    }
}

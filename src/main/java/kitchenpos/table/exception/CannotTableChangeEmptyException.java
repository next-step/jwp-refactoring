package kitchenpos.table.exception;

public class CannotTableChangeEmptyException extends RuntimeException {
    public CannotTableChangeEmptyException() {
        super("테이블이 비움상태를 수정할 수 없습니다.");
    }

    public CannotTableChangeEmptyException(String message) {
        super(message);
    }
}

package kitchenpos.table.exception;

public class CannotChangeEmptyException extends RuntimeException {

    public CannotChangeEmptyException() {
        super("비움 여부를 변경할 수 없습니다.");
    }
}

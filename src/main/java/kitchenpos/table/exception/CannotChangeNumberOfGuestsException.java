package kitchenpos.table.exception;

public class CannotChangeNumberOfGuestsException extends RuntimeException {

    public CannotChangeNumberOfGuestsException() {
        super("손님 수를 변경할 수 없습니다.");
    }
}

package kitchenpos.table.exception;

public class InvalidNumberOfGuestsException extends RuntimeException {

    public InvalidNumberOfGuestsException() {
        super("유효하지 않은 손님 수입니다.");
    }
}

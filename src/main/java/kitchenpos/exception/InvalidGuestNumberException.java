package kitchenpos.exception;

public class InvalidGuestNumberException extends IllegalArgumentException {
    public InvalidGuestNumberException() {
        super("잘못된 고객수입니다.");
    }

    public InvalidGuestNumberException(String s) {
        super(s);
    }
}

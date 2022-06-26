package kitchenpos.exception;

public class InvalidOrderStatusException extends IllegalArgumentException {
    public InvalidOrderStatusException() {
        super("완성된 상태는 변경할 수 없습니다.");
    }

    public InvalidOrderStatusException(String s) {
        super(s);
    }
}

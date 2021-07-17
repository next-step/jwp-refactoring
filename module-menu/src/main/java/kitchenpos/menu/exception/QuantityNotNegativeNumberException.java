package kitchenpos.menu.exception;

public class QuantityNotNegativeNumberException extends IllegalArgumentException {

    public QuantityNotNegativeNumberException() {
        super("갯수는 0보다 작을 수 없습니다.");
    }
}

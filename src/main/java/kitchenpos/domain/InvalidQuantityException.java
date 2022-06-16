package kitchenpos.domain;

public class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException() {
        super("유효하지 않은 수량입니다.");
    }
}

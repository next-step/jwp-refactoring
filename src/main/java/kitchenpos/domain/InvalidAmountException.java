package kitchenpos.domain;

public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException() {
        super("유효하지 않은 금액입니다.");
    }
}

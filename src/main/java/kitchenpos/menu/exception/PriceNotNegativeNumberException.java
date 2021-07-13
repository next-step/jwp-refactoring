package kitchenpos.menu.exception;

public class PriceNotNegativeNumberException extends IllegalArgumentException {

    public PriceNotNegativeNumberException() {
        super("가격은 0보다 작을 수 없습니다.");
    }

}

package kitchenpos.common.exception;

public class PriceNegativeException extends RuntimeException {

    public PriceNegativeException() {
        super("가격은 음수가 될 수 없습니다.");
    }
}

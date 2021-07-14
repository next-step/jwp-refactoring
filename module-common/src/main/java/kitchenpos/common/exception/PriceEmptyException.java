package kitchenpos.common.exception;

public class PriceEmptyException extends RuntimeException {

    public PriceEmptyException() {
        super("가격은 필수 입력 항목입니다.");
    }
}

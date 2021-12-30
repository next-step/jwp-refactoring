package kitchenpos.menu.exception;

public class WrongPriceException extends RuntimeException {
    public WrongPriceException() {
        super("가격이 0 이상이어야 합니다.");
    }
}

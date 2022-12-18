package kitchenpos.menu.exception;

public class MenuPriceGreaterThanAmountException extends RuntimeException {
    public MenuPriceGreaterThanAmountException(String message) {
        super(message);
    }
}

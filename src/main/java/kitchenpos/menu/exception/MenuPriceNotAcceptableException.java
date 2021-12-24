package kitchenpos.menu.exception;

public class MenuPriceNotAcceptableException extends RuntimeException {

    private final static String ERROR_MESSAGE_PRODUCT_PRICE_VALUE = "메뉴 가격은 0원 이상이어야 합니다.";

    public MenuPriceNotAcceptableException() {
        super(ERROR_MESSAGE_PRODUCT_PRICE_VALUE);
    }

    public MenuPriceNotAcceptableException(String message) {
        super(message);
    }
}

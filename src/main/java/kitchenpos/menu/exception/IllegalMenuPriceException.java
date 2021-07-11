package kitchenpos.menu.exception;

public class IllegalMenuPriceException extends RuntimeException{
    public IllegalMenuPriceException(String message) {
        super(message);
    }
}

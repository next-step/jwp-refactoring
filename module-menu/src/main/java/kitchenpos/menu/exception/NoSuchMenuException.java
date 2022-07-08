package kitchenpos.menu.exception;

public class NoSuchMenuException extends KitchenPosMenuException {
    private static final String ERROR_MESSAGE = "Menu가 존재하지 않습니다 (id: %d)";

    public NoSuchMenuException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}

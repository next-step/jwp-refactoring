package kitchenpos.menu.exception;

public class NoSuchMenuGroupException extends KitchenPosMenuException {
    private static final String ERROR_MESSAGE = "MenuGroup이 존재하지 않습니다 (id: %d)";

    public NoSuchMenuGroupException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}

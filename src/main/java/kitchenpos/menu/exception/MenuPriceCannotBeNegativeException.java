package kitchenpos.menu.exception;

public class MenuPriceCannotBeNegativeException extends RuntimeException {

    public MenuPriceCannotBeNegativeException() {
        super("메뉴의 가격은 음수가 될 수 없습니다.");
    }
}

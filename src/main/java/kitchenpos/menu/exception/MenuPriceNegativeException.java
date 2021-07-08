package kitchenpos.menu.exception;

public class MenuPriceNegativeException extends RuntimeException {

    public MenuPriceNegativeException() {
        super("메뉴의 가격은 음수가 될 수 없습니다.");
    }
}

package kitchenpos.menu.exception;

public class MenuProductQuantityNegativeException extends RuntimeException {

    public MenuProductQuantityNegativeException() {
        super("메뉴상품의 수량은 음수가 될 수 가 없습니다.");
    }
}

package kitchenpos.menu.exception;

public class MenuPriceEmptyException extends RuntimeException {

    public MenuPriceEmptyException() {
        super("메뉴의 가격은 필수 입력 항목입니다.");
    }
}

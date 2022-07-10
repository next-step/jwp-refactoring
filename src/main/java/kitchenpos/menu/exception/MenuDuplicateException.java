package kitchenpos.menu.exception;

public class MenuDuplicateException extends RuntimeException {
    private static final String message = "주문 시 주문 항목에 메뉴들은 중복될 수 없습니다.";

    public MenuDuplicateException() {
        super(message);
    }
}

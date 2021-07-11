package kitchenposNew.menu.exception;

public class NotFoundMenu extends RuntimeException {
    public NotFoundMenu() {
        super("찾을 수 없는 메뉴입니다.");
    }
}

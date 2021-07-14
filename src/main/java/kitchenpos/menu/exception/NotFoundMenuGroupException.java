package kitchenpos.menu.exception;

public class NotFoundMenuGroupException extends IllegalArgumentException {

    public NotFoundMenuGroupException() {
        super("메뉴그룹을 찾을 수 없습니다.");
    }

}

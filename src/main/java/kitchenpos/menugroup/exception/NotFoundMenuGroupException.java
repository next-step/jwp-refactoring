package kitchenpos.menugroup.exception;

public class NotFoundMenuGroupException extends RuntimeException {

    public NotFoundMenuGroupException() {
        super("메뉴 그룹을 찾을 수 없습니다.");
    }
}

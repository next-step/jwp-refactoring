package kitchenpos.menu.exception;

public class NotFoundMenuGroupException extends RuntimeException {
    public NotFoundMenuGroupException(Long menuGroupId) {
        super(menuGroupId + " 메뉴 그룹을 찾을 수 없습니다.");
    }
}

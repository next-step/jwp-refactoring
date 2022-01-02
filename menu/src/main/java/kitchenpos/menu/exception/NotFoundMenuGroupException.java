package kitchenpos.menu.exception;

import javax.persistence.EntityNotFoundException;

public class NotFoundMenuGroupException extends EntityNotFoundException {
    public NotFoundMenuGroupException(Long menuGroupId) {
        super(menuGroupId + " 메뉴 그룹을 찾을 수 없습니다.");
    }
}

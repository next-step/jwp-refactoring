package kitchenpos.menu.application.exception;

import kitchenpos.common.NotFoundException;

public class MenuGroupNotFoundException extends NotFoundException {
    public MenuGroupNotFoundException() {
        super("메뉴 그룹을 찾을 수 없습니다.");
    }

    public MenuGroupNotFoundException(String message) {
        super(message);
    }
}

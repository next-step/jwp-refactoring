package kitchenpos.menu.application.exception;

import kitchenpos.common.NotFoundException;

public class MenuNotFoundException extends NotFoundException {
    public MenuNotFoundException() {
        super("메뉴를 찾을 수 없습니다.");
    }

    public MenuNotFoundException(String message) {
        super(message);
    }
}

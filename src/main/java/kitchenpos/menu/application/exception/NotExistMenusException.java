package kitchenpos.menu.application.exception;

import kitchenpos.common.error.exception.NotExistException;

public class NotExistMenusException extends NotExistException {
    public NotExistMenusException() {
        super("메뉴 아이디에 해당하는 메뉴가 존재하지 않습니다.");
    }

    public NotExistMenusException(String message) {
        super(message);
    }
}

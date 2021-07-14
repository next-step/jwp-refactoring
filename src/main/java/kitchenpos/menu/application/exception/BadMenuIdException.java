package kitchenpos.menu.application.exception;

import kitchenpos.common.error.exception.BusinessException;

public class BadMenuIdException extends BusinessException {
    public BadMenuIdException() {
        super("메뉴 아이디가 잘못되었습니다.");
    }

    public BadMenuIdException(String message) {
        super(message);
    }
}

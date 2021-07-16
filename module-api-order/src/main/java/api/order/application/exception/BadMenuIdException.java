package api.order.application.exception;

import common.error.BusinessException;

public class BadMenuIdException extends BusinessException {
    public BadMenuIdException() {
        super("메뉴 아이디가 잘못되었습니다.");
    }

    public BadMenuIdException(String message) {
        super(message);
    }
}

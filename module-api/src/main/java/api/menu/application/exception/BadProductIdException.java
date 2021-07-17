package api.menu.application.exception;

import common.error.BusinessException;

public class BadProductIdException extends BusinessException {
    public BadProductIdException() {
        super("상품 아이디가 잘못되었습니다.");
    }

    public BadProductIdException(String message) {
        super(message);
    }
}

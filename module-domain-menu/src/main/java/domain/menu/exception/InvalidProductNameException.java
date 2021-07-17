package domain.menu.exception;

import common.error.BusinessException;

public class InvalidProductNameException extends BusinessException {
    public InvalidProductNameException() {
        super("상품이름을 지정해야합니다.");
    }

    public InvalidProductNameException(String message) {
        super(message);
    }
}

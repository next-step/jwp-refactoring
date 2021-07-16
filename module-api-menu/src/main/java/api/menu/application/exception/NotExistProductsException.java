package api.menu.application.exception;

import common.error.NotExistException;

public class NotExistProductsException extends NotExistException {
    public NotExistProductsException() {
        super("상품 아이디에 해당하는 상품이 존재하지 않습니다.");
    }

    public NotExistProductsException(String message) {
        super(message);
    }
}

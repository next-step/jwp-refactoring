package kitchenpos.menu.presentation.dto.exception;

import kitchenpos.common.error.exception.BusinessException;

public class BadProductIdException extends BusinessException {
    public BadProductIdException() {
        super("상품 아이디가 잘못되었습니다.");
    }

    public BadProductIdException(String message) {
        super(message);
    }
}

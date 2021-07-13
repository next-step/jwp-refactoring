package kitchenpos.menu.domain.exception;

import kitchenpos.common.error.exception.BusinessException;

public class InvalidMenuGroupNameException extends BusinessException {
    public InvalidMenuGroupNameException() {
        super("메뉴 그룹 이름을 지정해야합니다.");
    }

    public InvalidMenuGroupNameException(String message) {
        super(message);
    }
}

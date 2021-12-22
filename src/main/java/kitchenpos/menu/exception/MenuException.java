package kitchenpos.menu.exception;

import kitchenpos.common.exception.BusinessException;
import kitchenpos.common.exception.ErrorCode;

public class MenuException extends BusinessException {
	public MenuException(ErrorCode errorCode) {
		super(errorCode);
	}
}

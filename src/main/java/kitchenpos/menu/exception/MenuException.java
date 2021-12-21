package kitchenpos.menu.exception;

import kitchenpos.common.BusinessException;
import kitchenpos.common.ErrorCode;

public class MenuException extends BusinessException {
	public MenuException(ErrorCode errorCode) {
		super(errorCode);
	}
}

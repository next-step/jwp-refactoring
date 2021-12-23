package kitchenpos.order.exception;

import kitchenpos.common.exception.BusinessException;
import kitchenpos.common.exception.ErrorCode;

public class OrderException extends BusinessException {

	public OrderException(ErrorCode errorCode) {
		super(errorCode);
	}
}

package kitchenpos.order.exception;

import kitchenpos.common.BusinessException;
import kitchenpos.common.ErrorCode;

public class OrderException extends BusinessException {

	public OrderException(ErrorCode errorCode) {
		super(errorCode);
	}
}

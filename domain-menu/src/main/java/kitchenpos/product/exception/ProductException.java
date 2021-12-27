package kitchenpos.product.exception;

import kitchenpos.common.exception.BusinessException;
import kitchenpos.common.exception.ErrorCode;

public class ProductException extends BusinessException {

	public ProductException(ErrorCode errorCode) {
		super(errorCode);
	}
}

package kitchenpos.product.exception;

import kitchenpos.common.BusinessException;
import kitchenpos.common.ErrorCode;

public class ProductException extends BusinessException {

	public ProductException(ErrorCode errorCode) {
		super(errorCode);
	}
}

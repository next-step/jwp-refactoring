package kitchenpos.product.exception;

import kitchenpos.common.ErrorCode;

public class ProductException extends RuntimeException {

	public ProductException(ErrorCode errorCode) {
		super(errorCode.getMessage());
	}
}

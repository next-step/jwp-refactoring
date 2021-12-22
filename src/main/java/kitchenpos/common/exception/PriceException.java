package kitchenpos.common.exception;

public class PriceException extends BusinessException {
	public PriceException(ErrorCode errorCode) {
		super(errorCode);
	}
}

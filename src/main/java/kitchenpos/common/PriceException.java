package kitchenpos.common;

public class PriceException extends BusinessException {
	public PriceException(ErrorCode errorCode) {
		super(errorCode);
	}
}

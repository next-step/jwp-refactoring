package kitchenpos.table.exception;

import kitchenpos.common.exception.BusinessException;
import kitchenpos.common.exception.ErrorCode;

public class TableException extends BusinessException {

	public TableException(ErrorCode errorCode) {
		super(errorCode);
	}
}

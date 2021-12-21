package kitchenpos.tableGroup.exception;

import kitchenpos.common.BusinessException;
import kitchenpos.common.ErrorCode;

public class TableException extends BusinessException {
	public TableException(ErrorCode errorCode) {
		super(errorCode);
	}
}

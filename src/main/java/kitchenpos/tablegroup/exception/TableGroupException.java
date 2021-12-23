package kitchenpos.tablegroup.exception;

import kitchenpos.common.exception.BusinessException;
import kitchenpos.common.exception.ErrorCode;

public class TableGroupException extends BusinessException {
	public TableGroupException(ErrorCode errorCode) {
		super(errorCode);
	}
}

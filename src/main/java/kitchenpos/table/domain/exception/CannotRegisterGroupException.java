package kitchenpos.table.domain.exception;

import kitchenpos.common.error.exception.BusinessException;
public class CannotRegisterGroupException extends BusinessException {
    public CannotRegisterGroupException() {
        super("테이블 그룹을 등록할 수 없습니다.");
    }

    public CannotRegisterGroupException(String message) {
        super(message);
    }
}

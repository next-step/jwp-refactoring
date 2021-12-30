package kitchenpos.order.application.exception;

import kitchenpos.exception.NotFoundException;

public class TableGroupNotFoundException extends NotFoundException {
    public TableGroupNotFoundException() {
        super("테이블 그룹을 찾을 수 없습니다.");
    }

    public TableGroupNotFoundException(String message) {
        super(message);
    }
}

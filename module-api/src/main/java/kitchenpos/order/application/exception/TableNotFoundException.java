package kitchenpos.order.application.exception;

import kitchenpos.exception.NotFoundException;

public class TableNotFoundException extends NotFoundException {
    public TableNotFoundException() {
        super("주문 테이블을 찾을 수 없습니다.");
    }

    public TableNotFoundException(String message) {
        super(message);
    }
}

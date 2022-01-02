package kitchenpos.order.exception;

import javax.persistence.EntityNotFoundException;

public class NotCreatedMenuException extends EntityNotFoundException {
    public NotCreatedMenuException() {
        super("상품이 생성되어 있지 않습니다.");
    }
}

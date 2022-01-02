package kitchenpos.table.exception;

import javax.persistence.EntityNotFoundException;

public class NotFoundOrderTableException extends EntityNotFoundException {
    public NotFoundOrderTableException(Long id) {
        super(id + " 주문테이블이 존재하지 않습니다");
    }
}

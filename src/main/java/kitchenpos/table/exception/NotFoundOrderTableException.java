package kitchenpos.table.exception;

import java.io.Serializable;

public class NotFoundOrderTableException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -3817171920712274518L;

    public NotFoundOrderTableException(Long id) {
        super(id + "번 주문 테이블을 찾을 수 없습니다.");
    }
}

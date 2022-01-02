package table.exception;

import javax.persistence.EntityNotFoundException;

public class NotCreatedOrderTablesException extends EntityNotFoundException {
    public NotCreatedOrderTablesException() {
        super("주문테이블이 생성되어 있지 않습니다.");
    }
}

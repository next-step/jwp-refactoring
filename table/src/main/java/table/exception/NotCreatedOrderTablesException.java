package table.exception;

public class NotCreatedOrderTablesException extends RuntimeException {
    public NotCreatedOrderTablesException() {
        super("주문테이블이 생성되어 있지 않습니다.");
    }
}

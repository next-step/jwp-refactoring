package kitchenpos.exception;

public class IllegalOrderTablesException extends RuntimeException {
    public IllegalOrderTablesException() {
        super("주문 테이블이 적절하지 않습니다");
    }
}

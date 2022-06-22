package kitchenpos.exception;

public class EmptyOrderTablesException extends RuntimeException {
    public static final String DONT_EMPTY_IS_ORDER_TABLES = "주문 테이블(orderTables) 가 존재하지 않습니다.";

    public EmptyOrderTablesException() {
        super(DONT_EMPTY_IS_ORDER_TABLES);
    }
}

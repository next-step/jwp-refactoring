package kitchenpos.tablegroup.exception;

public class NotEnoughTablesException extends RuntimeException {

    private static final String ERROR_MESSAGE_NOT_ENOUGH_TABLES = "주문 테이블이 2개 이상일때 그룹화 가능 합니다.";

    public NotEnoughTablesException() {
        super(ERROR_MESSAGE_NOT_ENOUGH_TABLES);
    }
}

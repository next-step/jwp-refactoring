package kitchenpos.table.exception;

public enum OrderTablesExceptionType {

    EMPTY_TABLES("추가할 테이블이 없습니다"),
    LESS_THEN_MIN_TABLE_SIZE("테이블 개수가 부족합니다");

    public String message;

    OrderTablesExceptionType(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

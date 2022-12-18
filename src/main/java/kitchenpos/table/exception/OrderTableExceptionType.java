package kitchenpos.table.exception;

public enum OrderTableExceptionType {

    USING_TABLE_GROUP("사용중인 테이블그룹이 존재합니다"),
    NEGATIVE_NUMBER_OF_GEUEST("손님수는 0명이상 이어야합니다"),
    EMPTY_TABLE("테이블이 공석입니다");

    public String message;

    OrderTableExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

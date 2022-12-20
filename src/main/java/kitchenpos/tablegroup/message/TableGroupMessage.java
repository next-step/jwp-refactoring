package kitchenpos.tablegroup.message;

public enum TableGroupMessage {
    CREATE_ERROR_NOT_EQUAL_TABLE_SIZE("등록된 주문 테이블 개수가 다릅니다."),
    ;

    private final String message;

    TableGroupMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}

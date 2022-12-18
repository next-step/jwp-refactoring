package kitchenpos.tablegroup.message;

public enum TableGroupMessage {
    CREATE_ERROR_NOT_EQUAL_TABLE_SIZE("등록된 주문 테이블 개수가 다릅니다."),
    CREATE_ERROR_MORE_THAN_TWO_ORDER_TABLES("주문 테이블은 최소 2개 이상 주어져야 합니다."),
    CREATE_ERROR_OTHER_TABLE_GROUP_MUST_BE_NOT_ENROLLED("다른 테이블 그룹에 속한 테이블이 있으므로 생성할 수 없습니다."),
    ;

    private final String message;

    TableGroupMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}

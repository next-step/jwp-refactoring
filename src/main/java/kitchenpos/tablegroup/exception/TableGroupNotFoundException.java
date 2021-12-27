package kitchenpos.tablegroup.exception;

public class TableGroupNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE_NOT_EXIST_TABLE_GROUP = "테이블 그룹 정보가 없습니다.";

    public TableGroupNotFoundException() {
        super(ERROR_MESSAGE_NOT_EXIST_TABLE_GROUP);
    }
}

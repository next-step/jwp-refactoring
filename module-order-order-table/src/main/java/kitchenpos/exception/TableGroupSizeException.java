package kitchenpos.exception;

public class TableGroupSizeException extends RuntimeException {
    public static final TableGroupSizeException TABLE_GROUP_SIZE_EXCEPTION = new TableGroupSizeException(
            "단체 지정 할 테이블 목록의 개수는 2개 이상이어야 합니다.");

    public TableGroupSizeException(String message) {
        super(message);
    }
}

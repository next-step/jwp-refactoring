package kitchenpos.exception;

public class TableGroupNotFoundException extends RuntimeException {
    public static final String TABLE_GROUP_NOT_FOUND = "테이블 그룹을 찾을 수 없습니다.";

    public TableGroupNotFoundException() {
        super(TABLE_GROUP_NOT_FOUND);
    }
}

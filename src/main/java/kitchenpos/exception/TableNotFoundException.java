package kitchenpos.exception;

public class TableNotFoundException extends RuntimeException {
    public static final String TABLE_NOT_FOUND = "테이블을 찾을 수 없습니다.";

    public TableNotFoundException() {
        super(TABLE_NOT_FOUND);
    }
}

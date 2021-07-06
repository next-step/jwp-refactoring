package kitchenpos.tablegroup.exception;

public class NoTableGroupException extends IllegalArgumentException {
    private static final Long serialVersionUID = 540236956800849912L;
    private static final String NO_TABLE_GROUP = "존재하지 않는 테이블 그룹입니다.";

    public NoTableGroupException() {
        super(NO_TABLE_GROUP);
    }

    public NoTableGroupException(String message) {
        super(message);
    }
}

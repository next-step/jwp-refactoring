package kitchenpos.table.exception;

public class AlreadyExsistGroupException extends IllegalArgumentException {
    private static final Long serialVersionUID = 540236956800849912L;
    private static final String ALREADY_TABLE_GROUP = "이미 테이블이 특정 그룹에 속해있을 때, 테이블을 업데이트 할 수 없습니다.";

    public AlreadyExsistGroupException() {
        super(ALREADY_TABLE_GROUP);
    }

    public AlreadyExsistGroupException(String message) {
        super(message);
    }
}

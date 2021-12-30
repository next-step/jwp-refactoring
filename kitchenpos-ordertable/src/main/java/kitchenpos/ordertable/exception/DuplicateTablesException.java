package kitchenpos.ordertable.exception;

public class DuplicateTablesException extends RuntimeException {

    private static final String ERROR_MESSAGE_DUPLICATE_TALBES = "그룹대상 테이블에 중복이 존재합니다.";

    public DuplicateTablesException() {
        super(ERROR_MESSAGE_DUPLICATE_TALBES);
    }
}

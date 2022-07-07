package kitchenpos.exception;

public class NotFoundTableGroupException extends RuntimeException {
    private static final String message = "존재하지 않는 단체 지정입니다.";

    public static final NotFoundTableGroupException NOT_FOUND_TABLE_GROUP_EXCEPTION = new NotFoundTableGroupException(
            message);

    public NotFoundTableGroupException() {
        super(message);
    }

    public NotFoundTableGroupException(String message) {
        super(message);
    }
}

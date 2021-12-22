package kitchenpos.common.exception;

public class IsNotNullTableGroupException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "테이블 그룹이 이미 지정되어 있습니다.";

    public IsNotNullTableGroupException() {
        super(EXCEPTION_MESSAGE);
    }
}

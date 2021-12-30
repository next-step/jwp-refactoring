package kitchenpos.common.exceptions;

public class NotFoundEntityException extends CustomException {
    public static final String NOT_FOUND_ENTITY_MESSAGE = "조회한 내용은 존재하지 않습니다.";

    public NotFoundEntityException() {
        super(NOT_FOUND_ENTITY_MESSAGE);
    }
}

package kitchenpos.common.exceptions;

public class NotFoundEntityException extends CustomException {
    public static final String NOT_FOUND_ENTITY_MESSAGE = "찾을 수 없는 entity 입니다.";

    public NotFoundEntityException() {
        super(NOT_FOUND_ENTITY_MESSAGE);
    }
}

package kitchenpos.common.domain;

public class EntityNotFoundException extends RuntimeException {

    public static final String MESSAGE = "%s 엔티티를 찾을 수 없습니다.";

    public EntityNotFoundException(String entityName) {
        super(String.format(MESSAGE, entityName));
    }
}

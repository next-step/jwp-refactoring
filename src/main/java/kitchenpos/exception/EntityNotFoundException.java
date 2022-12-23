package kitchenpos.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityName, Long entityId) {
        super(String.format("해당 엔티티를 찾을 수 없습니다. 엔티티명 [%s], 엔티티 아이디 [%s]", entityName, entityId));
    }
}

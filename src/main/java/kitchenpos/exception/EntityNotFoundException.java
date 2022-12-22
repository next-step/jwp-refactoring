package kitchenpos.exception;

public class EntityNotFoundException extends CustomException {
    public EntityNotFoundException(String entityName, Long entityId) {
        super(ErrorMessage.notFoundEntity(entityName, entityId));
    }
}

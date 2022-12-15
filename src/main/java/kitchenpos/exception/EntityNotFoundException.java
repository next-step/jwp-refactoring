package kitchenpos.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entity, Long id) {
        super(ErrorMessage.notFoundEntity(entity, id));
    }
}

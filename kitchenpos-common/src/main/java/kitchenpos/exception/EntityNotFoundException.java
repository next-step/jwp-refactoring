package kitchenpos.exception;

public class EntityNotFoundException extends RuntimeException {

    private final EntityNotFoundExceptionCode code;

    public EntityNotFoundException(EntityNotFoundExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public EntityNotFoundExceptionCode getCode() {
        return this.code;
    }
}

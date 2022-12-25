package kitchenpos.exception;

public class EntityNotFoundException extends javax.persistence.EntityNotFoundException {

    private final EntityNotFoundExceptionConstants code;

    public EntityNotFoundException(EntityNotFoundExceptionConstants code) {
        super(code.getErrorMessage());
        this.code = code;
    }

    public EntityNotFoundExceptionConstants getCode() {
        return this.code;
    }
}

package kitchenpos.exception;

public class TableInUseException extends BadRequestException {
    public TableInUseException(String message) {
        super(message);
    }
}

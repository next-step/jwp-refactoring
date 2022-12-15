package kitchenpos.exception;

public class ChangeEmptyGroupException extends BadRequestException {

    public ChangeEmptyGroupException() {
        super(ErrorMessage.NOT_ALLOWED_CHANGE_EMPTY_WHEN_GROUP);
    }
}

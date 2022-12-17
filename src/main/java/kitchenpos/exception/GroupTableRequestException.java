package kitchenpos.exception;

public class GroupTableRequestException extends BadRequestException {

    public GroupTableRequestException() {
        super(ErrorMessage.GROUP_TABLE_REQUEST_ERROR);
    }
}

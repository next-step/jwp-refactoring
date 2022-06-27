package kitchenpos.table.exception;

public class NotFoundTableGroupException extends RuntimeException {
    public static final String NOT_FOUND_TABLE_GROUP_MESSAGE = "해당하는 단체를 찾을 수 없습니다. (id = %s)";

    public NotFoundTableGroupException(Long id) {
        super(String.format(NOT_FOUND_TABLE_GROUP_MESSAGE, id));
    }
}

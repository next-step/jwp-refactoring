package kitchenpos.tablegroup.exception;

public class NotFoundTableGroupException extends IllegalArgumentException{
    public NotFoundTableGroupException() {
        super();
    }

    public NotFoundTableGroupException(String string) {
        super(string);
    }
}

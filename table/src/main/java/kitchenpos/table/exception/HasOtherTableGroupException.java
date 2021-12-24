package kitchenpos.table.exception;

public class HasOtherTableGroupException extends IllegalArgumentException{
    public HasOtherTableGroupException() {
        super();
    }

    public HasOtherTableGroupException(String string) {
        super(string);
    }
}

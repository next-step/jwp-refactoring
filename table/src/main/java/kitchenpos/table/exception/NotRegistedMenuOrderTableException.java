package kitchenpos.table.exception;

public class NotRegistedMenuOrderTableException extends IllegalArgumentException{
    public NotRegistedMenuOrderTableException() {
        super();
    }

    public NotRegistedMenuOrderTableException(String string) {
        super(string);
    }
}

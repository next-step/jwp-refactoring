package kitchenpos.exception.table;

public class NotRegistedMenuOrderTableException extends IllegalArgumentException{
    public NotRegistedMenuOrderTableException() {
        super();
    }

    public NotRegistedMenuOrderTableException(String string) {
        super(string);
    }
}

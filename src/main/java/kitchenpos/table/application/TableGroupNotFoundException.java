package kitchenpos.table.application;

public class TableGroupNotFoundException extends RuntimeException {
    public TableGroupNotFoundException() {
    }

    public TableGroupNotFoundException(String message) {
        super(message);
    }
}

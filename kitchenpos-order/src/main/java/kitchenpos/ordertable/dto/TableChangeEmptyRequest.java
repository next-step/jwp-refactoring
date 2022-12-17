package kitchenpos.ordertable.dto;

public class TableChangeEmptyRequest {
    private boolean empty;

    public TableChangeEmptyRequest() {}

    public TableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
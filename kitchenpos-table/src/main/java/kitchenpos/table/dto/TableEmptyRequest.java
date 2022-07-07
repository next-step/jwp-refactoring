package kitchenpos.table.dto;

public class TableEmptyRequest {
    private boolean empty;

    public TableEmptyRequest() {
    }

    public TableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}

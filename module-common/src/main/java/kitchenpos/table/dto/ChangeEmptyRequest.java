package kitchenpos.table.dto;

public class ChangeEmptyRequest {
    private boolean empty;

    protected ChangeEmptyRequest() {
    }

    public ChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}

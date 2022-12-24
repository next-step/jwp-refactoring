package kitchenpos.table.dto;

public class ChangeEmptyRequest {
    private boolean empty;

    protected ChangeEmptyRequest() {}

    public ChangeEmptyRequest(boolean isEmpty) {
        this.empty = isEmpty;
    }

    public boolean isEmpty() {
        return empty;
    }
}

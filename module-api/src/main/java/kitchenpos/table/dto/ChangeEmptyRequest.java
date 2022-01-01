package kitchenpos.table.dto;

public class ChangeEmptyRequest {

    private boolean empty;

    public ChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public ChangeEmptyRequest() {
    }

    public boolean isEmpty() {
        return empty;
    }
}

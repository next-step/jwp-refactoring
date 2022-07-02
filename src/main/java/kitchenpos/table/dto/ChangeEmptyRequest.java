package kitchenpos.table.dto;

public class ChangeEmptyRequest {
    private boolean empty;

    protected ChangeEmptyRequest() {
    }

    private ChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public static ChangeEmptyRequest of(boolean empty) {
        return new ChangeEmptyRequest(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}

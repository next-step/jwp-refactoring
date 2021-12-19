package kitchenpos.table.dto;

public class ChangeTableEmptyRequest {
    private boolean empty;

    public ChangeTableEmptyRequest() {
    }

    private ChangeTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public static ChangeTableEmptyRequest of(boolean empty) {
        return new ChangeTableEmptyRequest(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}

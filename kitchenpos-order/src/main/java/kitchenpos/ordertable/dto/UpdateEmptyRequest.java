package kitchenpos.ordertable.dto;

public class UpdateEmptyRequest {
    private boolean empty;

    protected UpdateEmptyRequest() {}

    public UpdateEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public static UpdateEmptyRequest of(boolean empty) {
        return new UpdateEmptyRequest(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}

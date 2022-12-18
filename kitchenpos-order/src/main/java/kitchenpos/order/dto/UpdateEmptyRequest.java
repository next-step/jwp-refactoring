package kitchenpos.order.dto;

public class UpdateEmptyRequest {
    private boolean empty;

    protected UpdateEmptyRequest() {}

    private UpdateEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public static UpdateEmptyRequest of(boolean empty) {
        return new UpdateEmptyRequest(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}

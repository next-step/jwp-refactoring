package kitchenpos.table.dto;

public class OrderTableEmptyRequest {
    private boolean empty;

    public OrderTableEmptyRequest() {
    }

    public OrderTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}

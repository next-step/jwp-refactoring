package kitchenpos.table.dto;

public class OrderTableChangEmptyRequest {
    private final boolean empty;

    public OrderTableChangEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}

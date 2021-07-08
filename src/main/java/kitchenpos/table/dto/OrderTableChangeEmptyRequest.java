package kitchenpos.table.dto;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    public OrderTableChangeEmptyRequest() {
        // empty
    }

    public OrderTableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}

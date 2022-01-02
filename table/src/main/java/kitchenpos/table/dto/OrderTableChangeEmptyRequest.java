package kitchenpos.table.dto;

public class OrderTableChangeEmptyRequest {
    private boolean empty;

    protected OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public static OrderTableChangeEmptyRequest of(boolean empty) {
        return new OrderTableChangeEmptyRequest(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}

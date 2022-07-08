package kitchenpos.ordertable.dto;

public class OrderTableEmptyRequest {
    boolean empty;

    protected OrderTableEmptyRequest() {
    }

    private OrderTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public static OrderTableEmptyRequest from(boolean empty) {
        return new OrderTableEmptyRequest(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}

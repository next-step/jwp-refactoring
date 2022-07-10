package kitchenpos.table.dto;

public class OrderTableStatusRequest {
    private boolean empty;

    private OrderTableStatusRequest() {
    }

    public OrderTableStatusRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}

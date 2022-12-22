package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {

    private int guestCounts;
    private boolean empty;

    private OrderTableRequest(int guestCounts, boolean empty) {
        this.guestCounts = guestCounts;
        this.empty = empty;
    }

    public static OrderTableRequest of(int guestCounts, boolean empty) {
        return new OrderTableRequest(guestCounts, empty);
    }

    public OrderTable toEntity() {
        return OrderTable.of(guestCounts, empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}

package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableResponse {

    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableResponse(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getNumberOfGuests().value(),
                orderTable.isEmpty()
        );
    }

    public static List<OrderTableResponse> list(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

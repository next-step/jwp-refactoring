package kitchenpos.ordertable.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableResponse {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableResponse() {
    }

    private OrderTableResponse(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTableResponse> toOrderTableResponse(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable -> OrderTableResponse.from(orderTable))
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

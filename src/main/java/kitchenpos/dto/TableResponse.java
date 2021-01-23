package kitchenpos.dto;

import kitchenpos.domain.table.GuestNumber;
import kitchenpos.domain.table.OrderTable;

import static java.util.stream.Collectors.*;

import java.util.List;

public class TableResponse {
    private Long id;
    private List<OrderResponse> orderResponses;
    private int guestNumber;
    private boolean empty;

    public TableResponse(Long id, List<OrderResponse> orderResponses, GuestNumber guestNumber, boolean empty) {
        this.id = id;
        this.orderResponses = orderResponses;
        this.guestNumber = guestNumber.getGuestNumber();
        this.empty = empty;
    }

    public static TableResponse of(OrderTable orderTable) {
        return new TableResponse(orderTable.getId(),
                OrderResponse.ofList(orderTable.getOrders()),
                orderTable.getGuestNumber(),
                orderTable.isEmpty());
    }

    public static List<TableResponse> ofList(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(TableResponse::of)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }

    public int getGuestNumber() {
        return guestNumber;
    }

    public boolean isEmpty() {
        return empty;
    }
}

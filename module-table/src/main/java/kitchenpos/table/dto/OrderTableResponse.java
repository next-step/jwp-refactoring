package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {
    private Long id;
    private Long tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public static OrderTableResponse from(OrderTable orderTable) {
        OrderTableResponse response = new OrderTableResponse();

        response.id = orderTable.getId();
        response.numberOfGuests = orderTable.getNumberOfGuests().getValue();
        response.empty = orderTable.isEmpty();

        if (orderTable.getTableGroup() != null) {
            response.tableGroup = orderTable.getTableGroup().getId();
        }

        return response;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}

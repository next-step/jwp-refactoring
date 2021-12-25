package kitchenpos.order.table.dto;

import kitchenpos.order.table.domain.OrderTable;

public class OrderTableRequest {

    private Long id;
    private Integer numberOfGuests;
    private boolean empty;

    public Long getId() {
        return id;
    }

    public OrderTable toEntity() {
        return OrderTable.create(numberOfGuests, empty);
    }
}

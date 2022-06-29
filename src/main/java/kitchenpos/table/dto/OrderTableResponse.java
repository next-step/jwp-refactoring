package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {
    private Long id;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id) {
        this.id = id;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId());
    }

    public OrderTable toOrderTable() {
        return new OrderTable(id);
    }

    public Long getId() {
        return id;
    }
}

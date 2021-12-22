package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private String empty;

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(),
            orderTable.getTableGroup() != null ? orderTable.getTableGroup().getId() : null,
            orderTable.getNumberOfGuests(),
            orderTable.getOrderTableStatus().name());
    }

    private OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, String empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public String isEmpty() {
        return empty;
    }
}

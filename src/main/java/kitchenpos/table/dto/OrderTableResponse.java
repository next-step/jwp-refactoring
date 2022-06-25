package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {
    private Long id;
    private Integer numberOfGuests;
    private Boolean empty;
    private Long tableGroupId;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableResponse(Long id, Integer numberOfGuests, Boolean empty, Long tableGroupId) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        if (orderTable.getTableGroup() != null) {
            return new OrderTableResponse(orderTable.getId(),
                    orderTable.getNumberOfGuests().getNumberOfGuests(),
                    orderTable.getEmpty().isEmpty(),
                    orderTable.getTableGroup().getId());
        }
        return new OrderTableResponse(orderTable.getId(),
                orderTable.getNumberOfGuests().getNumberOfGuests(),
                orderTable.getEmpty().isEmpty());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}

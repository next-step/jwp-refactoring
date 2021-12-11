package kitchenpos.dto;

import kitchenpos.domain.order.OrderTable;

public class OrderTableDto {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableDto() {
    }

    private OrderTableDto(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId; 
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableDto of(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTableDto(id, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTableDto of(OrderTable orderTable) {
        return new OrderTableDto(orderTable.getId(), orderTable.getTableGroup().getId(), orderTable.getNumberOfGuests(), orderTable.getEmpty());
    }

    public Long getId() {
        return this.id;
    }

    public Long getTableGroupId() {
        return this.tableGroupId;
    }

    public int getNumberOfGuests() {
        return this.numberOfGuests;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean getEmpty() {
        return this.empty;
    }
}

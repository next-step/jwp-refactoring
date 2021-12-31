package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {
    private Long tableGroupId;
    
    private int numberOfGuests;
    
    private boolean empty;
    
    private OrderTableRequest() {
    }

    private OrderTableRequest(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }
    
    public static OrderTableRequest of(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTableRequest(tableGroupId, numberOfGuests, empty);
    }
    
    public static OrderTableRequest from(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }
    
    public OrderTable toOrderTable() {
        return OrderTable.of(numberOfGuests, empty);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

}

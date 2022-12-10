package kitchenpos.order.dto;

import kitchenpos.common.domain.Empty;
import kitchenpos.common.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse() {}

    private OrderTableResponse(Long id, Long tableGroupId, NumberOfGuests numberOfGuests, Empty empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests.value();
        this.empty = empty.isEmpty();
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.findTableGroupId(), orderTable.getNumberOfGuests(), orderTable.getEmpty());
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

    public boolean isEmpty() {
        return empty;
    }
}

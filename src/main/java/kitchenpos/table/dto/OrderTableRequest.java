package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableRequest {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
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

    public boolean isEmpty() {
        return empty;
    }

    public static OrderTableRequest of(OrderTable orderTable) {
        TableGroup tableGroup = orderTable.getTableGroup();
        Long tableGroupId = null;
        if (tableGroup != null) {
            tableGroupId = tableGroup.getId();
        }

        return new OrderTableRequest(orderTable.getId(), tableGroupId, orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }
}
package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private static Long getTableGroupIdByOrderTable(OrderTable orderTable) {
        Long tableGroupId = null;
        TableGroup tableGroup = orderTable.getTableGroup();
        if (tableGroup != null) {
            tableGroupId = tableGroup.getId();
        }
        return tableGroupId;
    }

    public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        Long tableGroupId = getTableGroupIdByOrderTable(orderTable);
        return new OrderTableResponse(orderTable.getId(), tableGroupId, orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }
}
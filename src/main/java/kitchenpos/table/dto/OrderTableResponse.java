package kitchenpos.table.dto;

import java.util.Collections;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse() {
        // empty
    }

    private OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(final OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(),
                                      getTableGroupId(orderTable),
                                      orderTable.getNumberOfGuests(),
                                      orderTable.isEmpty());
    }

    private static Long getTableGroupId(final OrderTable orderTable) {
        return orderTable.getTableGroup()
                         .orElseGet(() -> new TableGroup(Collections.emptyList()))
                         .getId();
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

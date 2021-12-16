package kitchenpos.dto.table;

import java.util.Objects;
import java.util.Optional;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableResponse() {}

    private OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(),
                                      orderTable.getTableGroupId(),
                                      orderTable.getNumberOfGuests().getValue(),
                                      orderTable.isEmpty());
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderTableResponse that = (OrderTableResponse)o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id)
            && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}

package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        if (tableGroup != null) {
            this.tableGroupId = tableGroup.getId();
        }
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroup(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTableResponse> ofList(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTableResponse that = (OrderTableResponse) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}

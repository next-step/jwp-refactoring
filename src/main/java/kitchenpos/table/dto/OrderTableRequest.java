package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableRequest {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(int numberOfGuests) {
        this(null, null, numberOfGuests, false);
    }

    public OrderTableRequest(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static List<OrderTableRequest> listOf(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTableRequest::of)
            .collect(Collectors.toList());
    }

    public static OrderTableRequest of(OrderTable orderTable) {
        Long orderTableId = null;
        if (orderTable.getTableGroup() != null) {
            orderTableId = orderTable.getTableGroup().getId();
        }
        return new OrderTableRequest(orderTable.getId(), orderTableId, orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTable> toOrderTables(List<OrderTableRequest> orderTables) {
        return orderTables.stream()
            .map(OrderTableRequest::toOrderTable)
            .collect(Collectors.toList());
    }

    public OrderTable toOrderTable() {
        return new OrderTable(new TableGroup(tableGroupId), numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    @Override
    public String toString() {
        return "OrderTableRequest{" +
            "id=" + id +
            ", tableGroupId=" + tableGroupId +
            ", numberOfGuests=" + numberOfGuests +
            ", empty=" + empty +
            '}';
    }
}

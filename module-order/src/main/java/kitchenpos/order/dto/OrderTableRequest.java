package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderTable;

public class OrderTableRequest {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(int numberOfGuests) {
        this(null, null, numberOfGuests, true);
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
        return new OrderTableRequest(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTable> toOrderTables(List<OrderTableRequest> orderTables) {
        return orderTables.stream()
            .map(OrderTableRequest::toOrderTable)
            .collect(Collectors.toList());
    }

    public OrderTable toOrderTable() {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
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

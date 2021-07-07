package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;
    private List<OrderResponse> orders;

    public OrderTableResponse() {
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(
            orderTable.getId(),
            orderTable.getTableGroup()
                .map(TableGroup::getId)
                .orElse(null),
            orderTable.getNumberOfGuests(),
            orderTable.isEmpty(),
            orderTable.getOrders()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList()));
    }

    public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty,
            List<OrderResponse> orders) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = orders;
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

    public List<OrderResponse> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderResponse> orders) {
        this.orders = orders;
    }
}

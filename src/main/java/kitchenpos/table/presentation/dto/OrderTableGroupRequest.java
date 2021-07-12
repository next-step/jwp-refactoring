package kitchenpos.table.presentation.dto;

import kitchenpos.table.presentation.dto.exception.BadSizeOrderTableException;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableGroupRequest {
    static final int MIN_VALUE = 2;

    private List<OrderTableRequest> orderTables;

    protected OrderTableGroupRequest() {
    }

    public OrderTableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTableGroupRequest of(List<OrderTableRequest> orderTables) {
        return new OrderTableGroupRequest(orderTables);
    }

    public List<Long> getOrderTableIds() {
        validateSize();
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    private void validateSize() {
        if (orderTables.isEmpty() || orderTables.size() < MIN_VALUE) {
            throw new BadSizeOrderTableException();
        }
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public int getTableCount() {
        return orderTables.size();
    }
}

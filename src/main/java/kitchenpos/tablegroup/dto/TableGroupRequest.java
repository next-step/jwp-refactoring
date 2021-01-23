package kitchenpos.tablegroup.dto;

import kitchenpos.table.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables;

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    public void validateOrderTableRequestSize() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정할 주문 테이블은 2개 이상이어야 합니다.");
        }
    }

    public void validateSavedOrderTable(List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        if (savedOrderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup()))) {
            throw new IllegalArgumentException();
        }
    }
}

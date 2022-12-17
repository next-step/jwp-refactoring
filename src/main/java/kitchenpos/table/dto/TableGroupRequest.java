package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTables;

    private TableGroupRequest() {}

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public void validate(List<OrderTable> savedOrderTables) {
        validateParam();
        validateExist(savedOrderTables);
        validateOrderTable(savedOrderTables);
    }

    private void validateParam() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExist(List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTable(List<OrderTable> savedOrderTables) {
        for (OrderTable savedOrderTable : savedOrderTables) {
            validateEmptyOrderTable(savedOrderTable);
            validateIncludeTableGroup(savedOrderTable);
        }
    }

    private void validateEmptyOrderTable(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIncludeTableGroup(OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getOrderTableIds() {
        if (orderTables != null) {
            return orderTables.stream()
                    .map(OrderTableIdRequest::getId)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}

package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.exception.EmptyOrderTableException;
import kitchenpos.table.exception.MinimumOrderTableNumberException;
import kitchenpos.table.exception.NotEmptyOrderTableGroupException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class TableGroupValidator {
    private static final int MINIMUM = 2;

    private final TableService tableService;

    public TableGroupValidator(final TableService tableService) {
        this.tableService = tableService;
    }

    public void validateOrderTables(final TableGroupRequest request) {
        final List<OrderTable> orderTables = tableService.findAllById(request.getOrderTableIds());
        validateOrderTables(orderTables);
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new EmptyOrderTableException();
        }
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new EmptyOrderTableException();
        }
        if (orderTables.size() < MINIMUM) {
            throw new MinimumOrderTableNumberException();
        }
        if (hasAnyEmptyTable(orderTables)) {
            throw new NotEmptyOrderTableGroupException();
        }
    }

    private static boolean hasAnyEmptyTable(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(OrderTable::isEmpty);
    }
}

package kitchenpos.tablegroup.domain;

import kitchenpos.core.domain.DomainService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.InvalidOrderTablesException;
import kitchenpos.tablegroup.exception.InvalidTableGroupException;
import org.springframework.util.CollectionUtils;

import java.util.List;

@DomainService
public class TableGroupCreatingValidator {

    private static final int MIN_SIZE = 2;

    public void validate(List<Long> orderTableIds, List<OrderTable> orderTables) {
        validateNotEmptyIds(orderTables);
        validateExistsAllOrderTables(orderTableIds, orderTables);
        validateOrderTables(orderTables);
    }

    private void validateNotEmptyIds(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new InvalidTableGroupException("단체 지정할 테이블이 없습니다.");
        }
    }

    private List<OrderTable> validateExistsAllOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new InvalidOrderTablesException("존재하지 않는 테이블이 있습니다.");
        }
        return orderTables;
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_SIZE) {
            throw new InvalidOrderTablesException("테이블 갯수가 적습니다.");
        }
        if (hasNotEmptyOrGrouped(orderTables)) {
            throw new InvalidOrderTablesException(
                    "테이블 중에 빈 테이블이 아니거나 다른 단체에 지정된 테이블이 있습니다.");
        }
    }

    private boolean hasNotEmptyOrGrouped(List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(it -> !it.isEmpty() || it.isGrouped());
    }
}

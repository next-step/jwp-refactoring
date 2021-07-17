package kitchenpos.tablegroup.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;

@Component
public class TableGroupValidator {
    private static final int MINIMUM_TABLE_SIZE = 2;

    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final TableGroupRequest tableGroupRequest) {
        validateOrderTableIdSize(tableGroupRequest);
        final List<Long> ids = tableGroupRequest.ids();
        final List<OrderTable> orderTables = findAllOrderTable(ids);
        validateOrderTableSize(ids, orderTables);
        validateOrderTables(orderTables);
    }

    private boolean isNotSatisfyToTableGroup(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .anyMatch(orderTable -> orderTable.isOccupied() || orderTable.hasTableGroupId());
    }

    private void validateOrderTableIdSize(final TableGroupRequest tableGroupRequest) {
        if (tableGroupRequest.isEmptyOrderTables() || tableGroupRequest.orderTablesSize() < MINIMUM_TABLE_SIZE) {
            throw new IllegalArgumentException(MINIMUM_TABLE_SIZE + "개 이상 테이블이 있어야 그룹을 만들 수 있습니다.");
        }
    }

    private List<OrderTable> findAllOrderTable(final List<Long> ids) {
        return orderTableRepository.findAllByIdIn(ids);
    }

    private void validateOrderTableSize(final List<Long> ids, final List<OrderTable> orderTables) {
        if (ids.size() != orderTables.size()) {
            throw new IllegalArgumentException("등록되지 않은 테이블이 있습니다.");
        }
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        if (isNotSatisfyToTableGroup(orderTables)) {
            throw new IllegalArgumentException("이미 사용 중이거나 다른 그룹에 속한 테이블은 그룹으로 설정할 수 없습니다.");
        }
    }
}

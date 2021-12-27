package kitchenpos.order.application;

import kitchenpos.order.application.exception.InvalidOrderState;
import kitchenpos.order.application.exception.InvalidTableState;
import kitchenpos.order.application.exception.TableNotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableRepository;
import kitchenpos.order.domain.TableState;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TableGroupValidator {
    private final TableRepository tableRepository;

    public TableGroupValidator(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public void validate(List<Long> tableIds) {
        List<OrderTable> orderTables = getTables(tableIds);
        validateOrderTables(tableIds, orderTables);
        validateEmptyTable(orderTables);
        validateExistGroup(orderTables);
    }

    private void validateOrderTables(List<Long> tableIds, List<OrderTable> orderTables) {
        if (tableIds.size() != orderTables.size()) {
            throw new TableNotFoundException();
        }
    }

    public void validateTableState(List<OrderTable> orderTables) {
        boolean isAllCompleted = orderTables.stream()
                .allMatch(OrderTable::isCompleted);

        if (!isAllCompleted) {
            throw new InvalidOrderState("모든 주문 상태가 완료되지 않아 단체석을 해제할 수 없습니다.");
        }
    }

    private void validateEmptyTable(List<OrderTable> orderTables) {
        boolean isEmpty = orderTables.stream()
                .map(OrderTable::getTableState)
                .anyMatch(TableState::isEmpty);

        if (isEmpty) {
            throw new InvalidTableState("빈 테이블을 일행으로 지정할 수 없습니다.");
        }
    }

    private void validateExistGroup(List<OrderTable> orderTables) {
        boolean isExistGroup = orderTables.stream()
                .anyMatch(orderTable -> Objects.nonNull(orderTable.getTableGroupId()));

        if (isExistGroup) {
            throw new InvalidTableState("테이블에 일행이 있습니다.");
        }
    }

    private List<OrderTable> getTables(List<Long> tableIds) {
        return tableIds.stream()
                .map(this::getTable)
                .collect(Collectors.toList());
    }

    private OrderTable getTable(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(TableNotFoundException::new);
    }
}

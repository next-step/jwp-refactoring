package kitchenpos.tablegroup.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {
    private static final int MIN_GROUPING_CRITERIA = 2;
    private static final String NOT_EXIST_TABLE = "테이블이 존재하지 않습니다.";
    private static final String EXIST_NOT_COMPLETION_ORDER = "완료되지 않은 주문이 존재합니다.";
    private static final String REQUIRED_TABLE = "테이블은 2개 이상 지정되어야 합니다.";

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public List<GroupTable> groupingTable(List<Long> tableIds, Long tableGroupId) {
        validateTableIds(tableIds);
        List<OrderTable> tables = orderTableRepository.findAllByIdIn(tableIds);
        validateTable(tables, tableIds);
        tables.forEach(table -> table.groupBy(tableGroupId));
        return tables.stream()
                .map(table -> new GroupTable(table.getId(), table.getTableGroupId(), table.getNumberOfGuests(), table.isEmpty()))
                .collect(Collectors.toList());
    }

    private void validateTableIds(List<Long> tableIds) {
        if (tableIds.size() < MIN_GROUPING_CRITERIA) {
            throw new IllegalArgumentException(REQUIRED_TABLE);
        }
    }

    private void validateTable(List<OrderTable> tables, List<Long> tableIds) {
        if (tables.size() != tableIds.size()) {
            throw new IllegalArgumentException(NOT_EXIST_TABLE);
        }
    }

    public void ungroup(Long tableGroupId) {
        List<OrderTable> tables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        tables.forEach(table -> validateOrderStatus(table.getId()));
        tables.forEach(OrderTable::ungroup);
    }

    private void validateOrderStatus(Long tableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(tableId);
        orders.stream()
                .filter(order -> !order.isComplete())
                .findFirst()
                .ifPresent(order -> {
                    throw new IllegalArgumentException(EXIST_NOT_COMPLETION_ORDER);
                });
    }
}

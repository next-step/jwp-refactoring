package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderStatusException;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTables;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class TableGroupValidator {

    private final TableService tableService;
    private final OrderRepository orderRepository;

    public TableGroupValidator(TableService tableService, OrderRepository orderRepository) {
        this.tableService = tableService;
        this.orderRepository = orderRepository;
    }

    public void validateCreate(List<Long> orderTableIds) {
        OrderTables orderTables = OrderTables.of(tableService.findAllByIdIn(orderTableIds));

        validateOrderTables(orderTables);
        validateOrderTablesSize(orderTableIds, orderTables);
    }

    public void validateUngroup(Long tableGroupId) {
        OrderTables orderTables = OrderTables.of(tableService.findAllByTableGroupId(tableGroupId));

        validateOrderStatus(orderTables);
    }

    private void validateOrderTables(OrderTables orderTables) {
        orderTables.getOrderTables().forEach(orderTable -> {
            if (orderTable.getEmpty().isNotEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException("테이블이 비어있지 않거나, 테이블 그룹에 이미 속해 있습니다.");
            }
        });
    }

    private void validateOrderTablesSize(List<Long> orderTableIds, OrderTables orderTables) {
        if (orderTableIds.size() < 2) {
            throw new IllegalArgumentException("그룹화할 테이블은 2개 이상이어야 합니다.");
        }
        if (orderTableIds.size() != orderTables.getOrderTables().size()) {
            throw new IllegalArgumentException("그룹화할 주문 테이블 수가 일치하지 않습니다.");
        }
    }

    private void validateOrderStatus(OrderTables orderTables) {
        List<Long> orderTableIds = orderTables.getOrderTableIds();
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses)) {
            throw new OrderStatusException();
        }
    }
}

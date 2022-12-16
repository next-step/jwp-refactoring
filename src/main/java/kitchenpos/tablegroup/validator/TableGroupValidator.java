package kitchenpos.tablegroup.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableGroupValidator {
    private static final int GROUP_TABLE_MINIMUM_SIZE = 2;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreateTableGroup(TableGroupRequest tableGroupRequest) {
        List<Long> orderTablesIds = tableGroupRequest.getOrderTableIds();
        validateOrderTableIds(orderTablesIds);
        validateOrderTables(orderTablesIds);
    }

    private void validateOrderTableIds(List<Long> orderTablesIds) {
        if (CollectionUtils.isEmpty(orderTablesIds)) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLES_IS_EMPTY.getMessage());
        }

        if (orderTablesIds.size() < GROUP_TABLE_MINIMUM_SIZE) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLES_MINIMUM_IS_TWO.getMessage());
        }
    }

    private void validateOrderTables(List<Long> orderTablesIds) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllById(orderTablesIds));

        if (orderTablesIds.size() != orderTables.get().size()) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLE_IS_NOT_EXIST.getMessage());
        }

        if (orderTables.isNotEmpty()) {
            throw new IllegalArgumentException(ErrorCode.NOT_EMPTY_STATUS_IN_ORDER_TABLES.getMessage());
        }

        if (orderTables.hasGroup()) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLES_HAS_GROUP_TABLE.getMessage());
        }
    }

    public void validateUnGroup(TableGroup tableGroup) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        List<Order> orders = findAllOrderByOrderTableIds(orderTables);

        orders.forEach(Order::validateOrderStatusShouldComplete);
    }

    private List<Order> findAllOrderByOrderTableIds(List<OrderTable> orderTables) {
        List<Long> orderTablesIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        return orderRepository.findAllByOrderTableIdIn(orderTablesIds);
    }
}

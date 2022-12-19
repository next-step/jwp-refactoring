package kitchenpos.tablegroup.domain;

import java.util.List;
import kitchenpos.constants.ErrorMessages;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTables getSavedOrderTablesIfValid(List<Long> orderTableIds) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findByIdIn(orderTableIds));
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException(ErrorMessages.SOME_ORDER_TABLE_TO_GROUP_DOES_NOT_FOUND);
        }
        validateBeforeCreateTableGroup(orderTables);
        return orderTables;
    }

    public void validateBeforeCreateTableGroup(OrderTables orderTables) {
        if (orderTables.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.ORDER_TABLE_DOES_NOT_EXIST);
        }
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException(ErrorMessages.ORDER_TABLE_TO_GROUP_CANNOT_BE_LESS_THAN_TWO);
        }
        orderTables.stream().forEach(OrderTable::checkOrderTableGroupSetAble);
    }

    public void validateBeforeUngroup(OrderTables orderTables) {
        final List<Long> orderTableIds = orderTables.getOrderTableIds();
        if (orderRepository.findByOrderTableIdIn(orderTableIds).stream()
                .anyMatch(Order::isOrderNotComplete)) {
            throw new IllegalArgumentException(ErrorMessages.NOT_COMPLETED_ORDER_EXIST);
        }
    }
}
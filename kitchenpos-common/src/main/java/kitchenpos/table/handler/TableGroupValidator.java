package kitchenpos.table.handler;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.exception.NotChangeToEmptyThatCookingOrMealTable;
import kitchenpos.table.exception.NotEmptyOrExistTableGroupException;
import kitchenpos.table.exception.NotFoundOrderTableException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class TableGroupValidator {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateGroup(TableGroup tableGroup) {
        OrderTables orderTables = tableGroup.getOrderTables();
        List<Long> orderTableIds = orderTables.getOrderTableIds();
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상이어야 합니다.");
        }
        if (!isEmptyTableAndNotExistTableGroupId(getOrderTables(orderTableIds))) {
            throw new NotEmptyOrExistTableGroupException();
        }
    }

    private boolean isEmptyTableAndNotExistTableGroupId(List<OrderTable> orderTables) {
        return orderTables.stream()
                .allMatch(orderTable -> orderTable.isEmptyTableAndNotExistTableGroupId());
    }

    public void validateUngroup(TableGroup tableGroup) {
        List<Order> orders = getOrders(tableGroup.getOrderTables());
        if (isCookingOrMeal(orders)) {
            throw new NotChangeToEmptyThatCookingOrMealTable();
        }
    }

    private boolean isCookingOrMeal(List<Order> orders) {
        return orders.stream()
                .anyMatch(order -> order.isCookingOrMeal());
    }

    private List<OrderTable> getOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new NotFoundOrderTableException();
        }
        return orderTables;
    }

    private List<Order> getOrders(OrderTables orderTables) {
        List<Long> orderIds = orderTables.getOrderIds();
        return orderRepository.findAllByIdIn(orderIds);
    }
}

package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.event.OrderTableChangedEvent;
import kitchenpos.table.event.OrderTableEmptiedEvent;
import kitchenpos.table.event.TableGroupRemovedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@Transactional
public class OrderEventHandler {
    private final OrderRepository orderRepository;

    public OrderEventHandler(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void orderStatusCheckedHandler(OrderTableChangedEvent event) {
        OrderTable orderTable = event.getOrderTable();

        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());

        if (isCookingAndMealStatus(orders)) {
            String errorMsg = String.format("%s 또는 %s 상태일때는 변경할수 없습니다.",
                    Order.OrderStatus.COOKING.remark(), Order.OrderStatus.MEAL.remark());

            throw new IllegalArgumentException(errorMsg);
        }

        orderTable.changeEmpty(event.isEmpty());
    }

    @EventListener
    public void orderTableEmptiedHandler(OrderTableEmptiedEvent event) {
        OrderTable orderTable = event.getOrderTable();

        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());

        if (isCookingAndMealStatus(orders)) {
            orderTable.clearTableGroup();
        }
    }

    @EventListener
    public void orderTableGroupUngroupdHandler(TableGroupRemovedEvent event) {
        OrderTables orderTables = event.getOrderTables();

        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTables.getOrderTableIds());

        if (isCookingAndMealStatus(orders) || orders.size() == 0) {
            orderTables.clearTableGroup();
        }
    }

    private boolean isCookingAndMealStatus(List<Order> orders) {
        return orders.stream().anyMatch(Order::isNotCompletionStatus);
    }
}

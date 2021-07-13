package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.event.TableGroupRemovedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OrderTableGroupUngroupdEventHandler {
    private final OrderRepository orderRepository;

    public OrderTableGroupUngroupdEventHandler(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    @EventListener
    public void handle(TableGroupRemovedEvent event) {
        OrderTables orderTables = event.getOrderTables();

        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTables.getOrderTableIds());

        //TODO
        if (isCookingAndMealStatus(orders) || orders.size() == 0) {
            orderTables.clearTableGroup();
        }
    }

    private boolean isCookingAndMealStatus(List<Order> orders) {
        return orders.stream().anyMatch(Order::isNotCompletionStatus);
    }

}

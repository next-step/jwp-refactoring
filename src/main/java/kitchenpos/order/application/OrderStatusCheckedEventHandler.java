package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.event.OrderTableChangedEvent;
import kitchenpos.table.domain.OrderTable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class OrderStatusCheckedEventHandler {
    private final OrderRepository orderRepository;

    public OrderStatusCheckedEventHandler(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(OrderTableChangedEvent event) {
        OrderTable orderTable = event.getOrderTable();

        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());

        if (isCookingAndMealStatus(orders)) {
            String errorMsg = String.format("%s 또는 %s 상태일때는 변경할수 없습니다.",
                    Order.OrderStatus.COOKING.remark(), Order.OrderStatus.MEAL.remark());

            throw new IllegalArgumentException(errorMsg);
        }

        orderTable.changeEmpty(event.isEmpty());
    }

    private boolean isCookingAndMealStatus(List<Order> orders) {
        return orders.stream().anyMatch(Order::isNotCompletionStatus);
    }
}

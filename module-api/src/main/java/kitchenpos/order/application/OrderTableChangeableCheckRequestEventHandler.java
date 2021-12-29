package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTableChangeableCheckRequestEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class OrderTableChangeableCheckRequestEventHandler {

    private final OrderRepository orderRepository;

    public OrderTableChangeableCheckRequestEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void checkOrderStatusCookingOrMeal(OrderTableChangeableCheckRequestEvent event) {

        List<Order> orders = orderRepository.findAllByOrderTableId(event.getOrderTableId());

        orders
                .forEach(Order::checkOrderStatusCookingOrMeal);

    }

}

package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTableChangeEmptyEvent;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class OrderTableChangeEmptyEventHandler {

    private final OrderRepository orderRepository;

    public OrderTableChangeEmptyEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void checkOrderStatusCookingOrMeal(OrderTableChangeEmptyEvent event) {

        List<Order> orders = orderRepository.findAllByOrderTableId(event.getOrderTableId());

        orders
                .forEach(Order::checkOrderStatusCookingOrMeal);

    }

}

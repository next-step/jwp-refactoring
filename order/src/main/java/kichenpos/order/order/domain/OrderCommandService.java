package kichenpos.order.order.domain;

import kichenpos.order.order.domain.event.OrderCreatedEvent;
import kichenpos.order.order.domain.event.OrderStatusChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final ApplicationEventPublisher eventPublisher;


    public OrderCommandService(OrderRepository orderRepository, OrderValidator orderValidator,
        ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.eventPublisher = eventPublisher;
    }

    public Order save(Order order) {
        orderValidator.validate(order);
        Order savedOrder = orderRepository.save(order);
        eventPublisher.publishEvent(OrderCreatedEvent.from(savedOrder));
        return order;
    }

    public Order changeStatus(long id, OrderStatus status) {
        Order order = orderRepository.order(id);
        order.changeStatus(status);
        eventPublisher.publishEvent(OrderStatusChangedEvent.from(order));
        return order;
    }

}

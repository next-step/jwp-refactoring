package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.event.OrderCreatedEvent;
import kitchenpos.order.event.OrderStatusChangedEvent;
import kitchenpos.order.exception.EmptyOrderTableException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public OrderService(
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository, ApplicationEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
    }

    public OrderResponse create(OrderCreateRequest orderCreateRequest) {
        OrderTable orderTable = generateOrderTable(orderCreateRequest);

        Order savedOrder = orderRepository.save(
                new Order(
                        orderTable,
                        OrderStatus.COOKING.name(),
                        LocalDateTime.now()
                )
        );

        publisher.publishEvent(new OrderCreatedEvent(savedOrder, orderCreateRequest.getOrderLineItems()));

        return OrderResponse.of(savedOrder);
    }

    private OrderTable generateOrderTable(OrderCreateRequest orderCreateRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(NoSuchElementException::new);

        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException();
        }
        return orderTable;
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream().map(OrderResponse::of).collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, OrderStatusChangeRequest orderStatusChangeRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);

        savedOrder.validateOrderStatusComplete();

        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusChangeRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        if(orderStatus.equals(OrderStatus.COMPLETION)) {
            publisher.publishEvent(new OrderStatusChangedEvent(orderId));
        }

        return OrderResponse.of(savedOrder);
    }
}

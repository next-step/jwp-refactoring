package kitchenpos.order.application;

import kitchenpos.order.domain.OrderCreateValidator;
import kitchenpos.order.event.OrderStatusEventPublisher;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.event.OrderStatusChangedEventImpl;
import kitchenpos.order.event.OrderStatusCreatedEventImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderCreateValidator orderCreateValidator;
    private final OrderStatusEventPublisher orderStatusEventPublisher;

    public OrderService(OrderRepository orderRepository, OrderCreateValidator orderCreateValidator,
            OrderStatusEventPublisher orderStatusEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderCreateValidator = orderCreateValidator;
        this.orderStatusEventPublisher = orderStatusEventPublisher;
    }

    @Transactional
    public Order create(final Order requestOrder) {
        orderCreateValidator.validate(requestOrder);
        final Order order = Order.of(requestOrder.getOrderTableId(), requestOrder.getOrderStatus(),
                requestOrder.getOrderedTime(), requestOrder.getOrderLineItemBag());
        order.updateItemOrder();
        final Order savedOrder = orderRepository.save(order);
        orderStatusEventPublisher.publish(
                new OrderStatusCreatedEventImpl(this, order.getId(), order.getOrderTableId(), order.getOrderStatus()));
        return savedOrder;

    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        checkedNullId(orderId);
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다"));
        savedOrder.changeStatus(order.getOrderStatus());
        orderStatusEventPublisher.publish(
                new OrderStatusChangedEventImpl(this, order.getId(), order.getOrderStatus()));
        return savedOrder;
    }

    private void checkedNullId(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("요청 주문 id는 null 이 아니어야 합니다");
        }
    }
}

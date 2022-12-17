package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderCreateValidator;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderCreateValidator orderCreateValidator;

    public OrderService(OrderRepository orderRepository, OrderCreateValidator orderCreateValidator) {
        this.orderRepository = orderRepository;
        this.orderCreateValidator = orderCreateValidator;
    }

    @Transactional
    public Order create(final Order requestOrder) {
        orderCreateValidator.validate(requestOrder);
        final Order order = Order.of(requestOrder.getOrderTableId(), requestOrder.getOrderStatus(),
                requestOrder.getOrderedTime(), requestOrder.getOrderLineItemBag());
        order.updateItemOrder();
        return orderRepository.save(order);

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
        return savedOrder;
    }

    private void checkedNullId(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("요청 주문 id는 null 이 아니어야 합니다");
        }
    }
}

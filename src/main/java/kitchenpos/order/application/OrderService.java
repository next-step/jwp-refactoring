package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository,
                        OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        Order order = new Order(orderRequest.getOrderTableId(), orderRequest.getOrderLineItems());
        orderValidator.validateCreation(order);

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        final List<Order> orders = orderRepository.findAllWithOrderLineItems();

        orders.forEach(order -> order
                .addLineItems(orderLineItemRepository.findAllByOrderId(order.getId()))
        );

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeStatus(orderStatusRequest.getOrderStatus());

        orderRepository.save(savedOrder);

        return savedOrder;
    }
}

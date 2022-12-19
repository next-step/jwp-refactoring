package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.mapper.OrderMapper;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderValidators orderValidators;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository,
                        OrderValidators orderValidators, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderValidators = orderValidators;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        Order order = orderMapper.mapFrom(orderRequest);
        orderValidators.validateCreation(order);

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

package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderLineItemGenerator orderLineItemGenerator;

    public OrderService(
        final OrderRepository orderRepository,
        final OrderValidator orderValidator,
        final OrderLineItemGenerator orderLineItemGenerator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderLineItemGenerator = orderLineItemGenerator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderValidator.validateSave(orderRequest);
        List<OrderLineItem> orderLineItems = orderLineItemGenerator.generate(orderRequest.getOrderLineItems());
        final Order order = generateOrder(orderRequest, orderLineItems);
        return OrderResponse.from(orderRepository.save(order));
    }

    private Order generateOrder(OrderRequest orderRequest, List<OrderLineItem> orderLineItems) {
        return Order.generate(
            orderRequest.getOrderTableId(),
            orderLineItems
        );
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.updateOrderStatus(order.getOrderStatus());
        return savedOrder;
    }
}

package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(orderRequest);
        orderValidator.validCreate(orderRequest.getOrderTableId(), orderLineItems);
        Order savedOrder = orderRepository.save(Order.of(orderRequest.getOrderTableId(), orderLineItems));
        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> mapToOrderLineItems(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems()
                .stream()
                .map(i -> OrderLineItem.of(i.getMenuId(), i.getQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest order) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.changeStatus(order.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }

    private Order findOrderById(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }


}

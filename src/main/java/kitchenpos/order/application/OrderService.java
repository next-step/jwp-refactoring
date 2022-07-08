package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(final OrderRepository orderRepository,
                        final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderLineItems orderLineItems = createOrderLineItems(orderRequest);
        Order order = Order.of(orderRequest.getOrderTableId(), orderLineItems);

        orderValidator.validate(order);

        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    private OrderLineItems createOrderLineItems(OrderRequest orderRequest) {
        OrderLineItems orderLineItems = OrderLineItems.create();
        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            orderLineItems.add(OrderLineItem.of(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()));
        }
        return orderLineItems;
    }

    public List<OrderResponse> listAllOrders() {
        return OrderResponse.from(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest order) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.updateOrderStatus(order.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("주문(%d)을 찾을 수 없습니다.", orderId)));
    }

    public boolean existOrderBeforeCompletion(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.COMPLETION));
    }

    public boolean existOrderBeforeCompletion(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}

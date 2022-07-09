package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuValidator menuValidator;
    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(MenuValidator menuValidator, OrderValidator orderValidator, OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository) {
        this.menuValidator = menuValidator;
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderValidator.validate(orderRequest);

        Order savedOrder = orderRepository.save(orderRequest.toEntity());

        menuValidator.validateOrderLineItems(getMenuIds(orderRequest.getOrderLineItemRequests()));

        OrderLineItems orderLineItems = new OrderLineItems(orderRequest.getOrderLineItems());
        orderLineItems.saveOrder(savedOrder);
        orderLineItemRepository.saveAll(orderLineItems.getOrderLineItems());

        return new OrderResponse(savedOrder);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = getOrder(orderId);

        savedOrder.changeOrderStatue(orderStatusRequest.getOrderStatus());

        return new OrderResponse(savedOrder);
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<Long> getMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }
}

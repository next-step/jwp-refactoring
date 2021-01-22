package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final OrderTableService orderTableService;

    public OrderService(MenuService menuService, OrderRepository orderRepository, OrderTableService orderTableService) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        OrderTable orderTable = orderTableService.findById(request.getOrderTableId());
        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
            .map(orderLineItemRequest -> new OrderLineItem(
                menuService.findById(orderLineItemRequest.getMenuId()),
                orderLineItemRequest.getQuantity()
            ))
            .collect(Collectors.toList());
        Order order = new Order(orderTable, request.getOrderStatus(), orderLineItems);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderRequest request) {
        Order savedOrder = findById(orderId);
        savedOrder.changeOrderStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
    }
}

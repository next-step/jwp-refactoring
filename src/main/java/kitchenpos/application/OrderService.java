package kitchenpos.application;

import kitchenpos.common.exceptions.NotFoundEntityException;
import kitchenpos.common.exceptions.OrderStatusNotProcessingException;
import kitchenpos.domain.*;
import kitchenpos.dto.menu.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final TableService tableService;

    public OrderService(
            final OrderRepository orderRepository, final MenuService menuService, final TableService tableService) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final OrderTable orderTable = tableService.findOrderTableById(request.getOrderTableId());
        final Order order = Order.from(orderTable);
        final List<OrderLineItem> orderLineItems = getOrderLineItems(request.getOrderLineItems());
        order.addOrderLineItems(orderLineItems);
        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> getOrderLineItems(final List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(this::getOrderItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem getOrderItem(final OrderLineItemRequest request) {
        final Menu menu = menuService.getMenuById(request.getMenuId());
        return OrderLineItem.of(menu, request.getQuantity());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findById(orderId);
        order.updateStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(order);
    }

    private Order findById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(NotFoundEntityException::new);
    }

    public void validateOrderStatus(final List<OrderTable> orderTables) {
        final List<Order> orders = orderRepository.findByOrderTableIn(orderTables);
        final List<Order> checkOrders = orders.stream()
                .filter(Order::existsOrderStatus)
                .collect(Collectors.toList());
        if (orders.containsAll(checkOrders)) {
            throw new OrderStatusNotProcessingException();
        }
    }
}

package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuService menuService, final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());

        final Order order = new Order(orderTable);
        order.addOrderLineItems(makeOrderLineItems(order, orderRequest.getOrderLineItems()));

        final Order persistOrder = orderRepository.save(order);
        return OrderResponse.of(persistOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findOrderById(orderId);

        OrderStatus changeOrderStatus = orderRequest.getOrderStatus();
        order.changeOrderStatus(changeOrderStatus);

        return OrderResponse.of(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public Order findOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public boolean isAllOrderStatusCompleted(final List<Long> orderTableIds) {
        return orderTableIds.stream()
            .allMatch(this::isOrderStatusCompleted);
    }

    @Transactional(readOnly = true)
    public boolean isOrderStatusCompleted(final Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);

        return orders.stream()
            .allMatch(Order::isCompleteStatus);
    }

    private List<OrderLineItem> makeOrderLineItems(final Order order,
        final List<OrderLineItemRequest> orderLineItemRequests) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menuService.findById(orderLineItemRequest.getMenuId());
            orderLineItems.add(new OrderLineItem(order, menu, orderLineItemRequest.getQuantity()));
        }

        return orderLineItems;
    }
}

package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderChangeStatusRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = findOrderTableAndEmptyIsFalse(orderRequest.getOrderTableId());
        OrderLineItems orderLineItems = findOrderLineItems(orderRequest.getOrderLineItems());
        //Order order = orderRequest.toOrder(orderTable, orderLineItems);

        //final OrderLineItems orderLineItems = new OrderLineItems(order.getOrderLineItems().getOrderLineItems());
        final List<Long> menuIds = orderLineItems.findMenuIds();
        orderLineItems.vaildateSize(menuCountByIdIn(menuIds));

        Order saveOrder = new Order(orderTable, OrderStatus.COOKING, orderLineItems);
        final Order savedOrder = saveOrder(saveOrder);

        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = findOrders();

        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest orderChangeStatusRequest) {
        final Order savedOrder = findOrder(orderId);
        savedOrder.updateOrderStatus(orderChangeStatusRequest.getOrderStatus());
        saveOrder(savedOrder);

        return OrderResponse.of(savedOrder);
    }

    private Order saveOrder(Order saveOrder) {
        return orderRepository.save(saveOrder);
    }

    private long menuCountByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

    private OrderTable findOrderTableAndEmptyIsFalse(Long orderTableId) {
        return orderTableRepository.findByIdAndEmptyIsFalse(orderTableId)
                .orElseThrow(NoSuchElementException::new);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
    }

    private List<Order> findOrders() {
        return orderRepository.findAll();
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
    }

    private OrderLineItems findOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        return new OrderLineItems(orderLineItems.stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList()));
    }

    private OrderLineItem createOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = findMenu(orderLineItemRequest.getMenuId());
        return new OrderLineItem(null, null, menu, orderLineItemRequest.getQuantity());
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(NoSuchElementException::new);
    }
}

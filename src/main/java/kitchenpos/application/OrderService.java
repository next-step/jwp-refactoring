package kitchenpos.application;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderLineItem.OrderLineItem;
import kitchenpos.domain.orderLineItem.OrderLineItemRepository;
import kitchenpos.domain.orderLineItem.OrderLineItems;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        Order order = orderRequest.toOrder(orderTable);

        final OrderLineItems orderLineItems = new OrderLineItems(order.getOrderLineItems().getOrderLineItems());
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
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        OrderTable ordertable = findOrderTable(orderRequest.getOrderTableId());
        Order order = orderRequest.toOrder(ordertable);

        final Order savedOrder = findOrder(orderId);

        savedOrder.updateOrderStatus(order.getOrderStatus());

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
}

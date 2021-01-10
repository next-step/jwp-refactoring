package kitchenpos.application;

import kitchenpos.domain.order.*;
import kitchenpos.domain.order.exceptions.OrderEntityNotFoundException;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final SafeMenu safeMenu;
    private final SafeOrderTable safeOrderTable;

    public OrderService(
            final OrderRepository orderRepository,
            final SafeMenu safeMenu,
            final SafeOrderTable safeOrderTable
    ) {
        this.orderRepository = orderRepository;
        this.safeMenu = safeMenu;
        this.safeOrderTable = safeOrderTable;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        validateCreateRequest(orderRequest);

        Order order = parseToOrder(orderRequest);

        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest orderStatusChangeRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderEntityNotFoundException("존재하지 않는 주문입니다."));

        final OrderStatus orderStatus = OrderStatus.find(orderStatusChangeRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.of(savedOrder);
    }

    private List<OrderLineItem> parserToOrderLineItem(final OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();

        return orderLineItemRequests.stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
    }

    private Order parseToOrder(final OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = parserToOrderLineItem(orderRequest);

        return new Order(orderRequest.getOrderTableId(), orderLineItems);
    }

    private void validateCreateRequest(final OrderRequest orderRequest) {
        safeMenu.isMenuExists(parserToOrderLineItem(orderRequest));
        safeOrderTable.canOrderAtThisTable(orderRequest.getOrderTableId());
    }
}

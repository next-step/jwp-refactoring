package kitchenpos.order.application;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.ExceptionMessage;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));

        final Order savedOrder = orderRepository.save(Order.of(
            orderTable.getId(), makeOrderLineItems(orderRequest.getOrderLineItems())));

        return new OrderResponse(savedOrder);
    }

    private List<OrderLineItem> makeOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream().map(
            orderLineItemRequest -> orderLineItemRequest.toEntity(findMenu(orderLineItemRequest.getMenuId()))
        ).collect(Collectors.toList());
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(() -> new NotFoundException(
            ExceptionMessage.NOT_FOUND_DATA));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findOrders();

        return orders.stream()
            .map(OrderResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));
        savedOrder.changeOrderStatus(orderStatusRequest.getOrderStatus());
        return new OrderResponse(savedOrder);
    }
}

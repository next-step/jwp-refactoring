package kitchenpos.application;

import kitchenpos.domain.order.*;
import kitchenpos.domain.order.exceptions.InvalidTryChangeOrderStatusException;
import kitchenpos.domain.order.exceptions.InvalidTryOrderException;
import kitchenpos.domain.order.exceptions.MenuEntityNotFoundException;
import kitchenpos.domain.order.exceptions.OrderEntityNotFoundException;
import kitchenpos.infra.menu.MenuDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.domain.exceptions.orderTable.OrderTableEntityNotFoundException;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderTableDao orderTableDao;
    private final OrderRepository orderRepository;
    private final SafeMenu safeMenu;
    private final SafeOrderTable safeOrderTable;

    public OrderService(
            final OrderTableDao orderTableDao,
            final OrderRepository orderRepository,
            final SafeMenu safeMenu,
            final SafeOrderTable safeOrderTable
    ) {
        this.orderTableDao = orderTableDao;
        this.orderRepository = orderRepository;
        this.safeMenu = safeMenu;
        this.safeOrderTable = safeOrderTable;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();

        List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        final Order order = new Order(orderRequest.getOrderTableId(), orderLineItems);

        safeMenu.isMenuExists(orderLineItems);
        safeOrderTable.canOrderAtThisTable(orderRequest.getOrderTableId());

        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

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

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new InvalidTryChangeOrderStatusException("계산 완료된 주문의 상태를 바꿀 수 없습니다.");
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusChangeRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        Order statusChangedOrder = orderRepository.save(savedOrder);

        return OrderResponse.of(statusChangedOrder);
    }
}

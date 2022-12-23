package kitchenpos.order.application;

import common.exception.NoSuchDataException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {

        final OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        Order order = new Order(OrderStatus.COOKING.name(), LocalDateTime.now(), orderTable);
        List<OrderLineItem> orderLineItemList = generateOrderLineItems(orderRequest.getOrderLineItems(), order);
        order.addOrderLineItems(orderLineItemList);

        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {

        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order persistOrder = findOrderById(orderId);
        persistOrder.updateOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(persistOrder);
    }

    private List<OrderLineItem> generateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests, Order order) {
        List<OrderLineItem> orderLineItemList = new ArrayList<>();
        orderLineItemRequests.stream()
                .forEach(orderLineItemRequest -> {
                    Menu menu = findMenuById(orderLineItemRequest.getMenuId());
                    orderLineItemList.add(new OrderLineItem(order, menu, orderLineItemRequest.getQuantity()));
                });

        return orderLineItemList;
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(NoSuchDataException::new);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(NoSuchDataException::new);
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(NoSuchDataException::new);
    }
}
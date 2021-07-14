package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.order.util.OrderMapper;
import kitchenpos.order.util.OrderValidator;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;

    public OrderService(OrderMapper orderMapper, OrderValidator orderValidator, OrderRepository orderRepository) {
        this.orderMapper = orderMapper;
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = orderMapper.mapFormToOrder(orderRequest);
        order.validateOrder(orderValidator);

        /*final List<Long> menuIds = orderRequest.getMenuIds();
        if (!orderRequest.isEqualsMenuSize(menuRepository.countByIdIn(menuIds))) {
            throw new NotEqualsOrderCountAndMenuCount();
        }
        final OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        List<Menu> menus = findMenusByIds(menuIds);
        List<OrderLineItem> orderLineItems = toOrderLineItem(menus, orderRequest);
        Order order = new Order(orderTable, new OrderLineItems(orderLineItems));
        final Order persistOrder = orderRepository.save(order);*/
        return OrderResponse.of(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(order -> OrderResponse.of(order))
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundOrderException());
        savedOrder.changeOrderStatusCooking();
        return OrderResponse.of(savedOrder);
    }

    /*private List<Menu> findMenusByIds(List<Long> menuIds) {
        return menuIds.stream()
                .map(menuId -> menuRepository.findById(menuId).orElseThrow(() -> new NotFoundMenuException()))
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundOrderTable());
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return orderTable;
    }

    private List<OrderLineItem> toOrderLineItem(List<Menu> menus, OrderRequest orderRequest) {
        return menus.stream()
                .map(menu -> findOrderLineItem(menu, orderRequest))
                .collect(Collectors.toList());
    }

    private OrderLineItem findOrderLineItem(Menu menus, OrderRequest orderRequest) {
        return orderRequest.getOrderLineItemRequests().stream()
                .filter(orderLineItemRequest -> orderLineItemRequest.getMenuId().equals(menus.getId()))
                .map(orderLineItemRequest -> new OrderLineItem(menus, orderLineItemRequest.getQuantity()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }*/
}

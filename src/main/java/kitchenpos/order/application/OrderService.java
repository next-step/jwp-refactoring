package kitchenpos.order.application;

import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_MENU;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER_TABLE;

import kitchenpos.exception.KitchenposException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());
        final Order savedOrder = orderRepository.save(new Order(orderTable.getId(),
                                                                OrderStatus.COOKING,
                                                                getOrderLineItems(orderRequest)));
        return OrderResponse.of(savedOrder);
    }

    private List<OrderLineItem> getOrderLineItems(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems()
                           .stream()
                           .map(this::createOrderLineItem)
                           .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = findMenuById(orderLineItemRequest.getMenuId());
        return new OrderLineItem(menu.getId(),
                                 orderLineItemRequest.getQuantity());
    }

    private Menu findMenuById(final Long menuId) {
        return menuRepository.findById(menuId)
                             .orElseThrow(() -> new KitchenposException(NOT_FOUND_MENU));
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                                   .orElseThrow(() -> new KitchenposException(NOT_FOUND_ORDER_TABLE));
    }


    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                              .stream()
                              .map(OrderResponse::of)
                              .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId,
                                           final OrderStatusRequest orderStatusRequest) {
        return OrderResponse.of(findOrderById(orderId).changeOrderStatus(orderStatusRequest.getOrderStatus()));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                              .orElseThrow(() -> new KitchenposException(NOT_FOUND_ORDER));
    }
}

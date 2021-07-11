package kitchenpos.order.application;

import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_MENU;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER_LINE_ITEM;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER_TABLE;

import kitchenpos.exception.KitchenposException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
        final Order savedOrder = orderRepository.save(new Order(findOrderTableById(orderRequest.getOrderTableId()),
                                                                OrderStatus.COOKING,
                                                                getOrderLineItems(orderRequest)));
        return OrderResponse.of(savedOrder);
    }

    private List<OrderLineItem> getOrderLineItems(OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItems = orderRequest.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new KitchenposException(NOT_FOUND_ORDER_LINE_ITEM);
        }

        return orderLineItems.stream()
                             .map(this::createOrderLineItem)
                             .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        return new OrderLineItem(findMenuById(orderLineItemRequest),
                                 orderLineItemRequest.getQuantity());
    }

    private Menu findMenuById(OrderLineItemRequest orderLineItemRequest) {
        return menuRepository.findById(orderLineItemRequest.getMenuId())
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

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        return OrderResponse.of(findOrderById(orderId).changeOrderStatus(orderStatusRequest.getOrderStatus()));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                              .orElseThrow(() -> new KitchenposException(NOT_FOUND_ORDER));
    }
}

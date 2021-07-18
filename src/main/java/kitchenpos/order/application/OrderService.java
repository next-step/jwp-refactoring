package kitchenpos.order.application;

import kitchenpos.common.exception.CannotFindException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.*;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.common.exception.Message.*;
import static kitchenpos.order.domain.OrderStatus.COOKING;

@Service
@Transactional
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

    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = findOrderTable(orderRequest);
        final Order savedOrder = orderRepository.save(new Order(orderTable.getId(), COOKING));
        final List<OrderLineItem> orderLineItems = getOrderLineItems(orderRequest, savedOrder);
        final List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.saveAll(orderLineItems);
        return toOrderResponse(savedOrder, toOrderLineItemResponses(savedOrderLineItems));
    }

    private List<OrderLineItemResponse> toOrderLineItemResponses(List<OrderLineItem> savedOrderLineItems) {
        return savedOrderLineItems.stream()
                .map(this::toOrderLineItemResponse)
                .collect(Collectors.toList());
    }

    private OrderLineItemResponse toOrderLineItemResponse(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity().value());
    }

    private List<OrderLineItem> getOrderLineItems(OrderRequest orderRequest, Order savedOrder) {
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemRequests().stream()
                .map(this::getOrderLineItem)
                .collect(Collectors.toList());

        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ERROR_ORDER_LINE_ITEMS_SHOULD_HAVE_AT_LEAST_ONE_ITEM.showText());
        }
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.ofOrder(savedOrder);
        }
        return orderLineItems;
    }

    private OrderLineItem getOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = findMenuById(orderLineItemRequest.getMenuId());
        return new OrderLineItem(menu.getId(), Quantity.of(orderLineItemRequest.getQuantity()));
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new CannotFindException(ERROR_ORDER_SHOULD_HAVE_REGISTERED_MENU));
    }

    private OrderTable findOrderTable(OrderRequest orderRequest) {
        return orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new CannotFindException(ERROR_ORDER_SHOULD_HAVE_REGISTERED_TABLE));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        final List<Long> orderIds = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());
        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderIdIn(orderIds);

        final List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderLineItem> itemsOfOrder = orderLineItems.stream()
                    .filter(orderLineItem -> orderLineItem.isItemOf(order))
                    .collect(Collectors.toList());
            orderResponses.add(toOrderResponse(order, toOrderLineItemResponses(itemsOfOrder)));
        }
        return orderResponses;
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.changeOrderStatus(orderStatusRequest.getOrderStatus());
        return toOrderResponse(savedOrder);
    }

    private OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus().name(),
                order.getOrderedTime());
    }

    private OrderResponse toOrderResponse(Order order, List<OrderLineItemResponse> orderLineItemResponses) {
        return new OrderResponse(order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                orderLineItemResponses);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CannotFindException(ERROR_ORDER_NOT_FOUND));
    }
}

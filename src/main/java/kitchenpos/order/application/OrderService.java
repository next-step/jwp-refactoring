package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository, OrderRepository orderRepository,
            OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItem> orderLineItems = generateOrderLineItems(orderCreateRequest);

        OrderTable orderTable = generateOrderTable(orderCreateRequest);

        Order savedOrder = generateOrder(orderTable, orderLineItems);

        return OrderResponse.of(savedOrder);
    }

    private Order generateOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        Order savedOrder = orderRepository.save(
                new Order(
                        orderTable,
                        OrderStatus.COOKING.name(),
                        LocalDateTime.now()
                )
        );

        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(savedOrder);
        }

        savedOrder.addOrderLineItems(orderLineItems);
        return savedOrder;
    }

    private OrderTable generateOrderTable(OrderCreateRequest orderCreateRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(NoSuchElementException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return orderTable;
    }

    private List<OrderLineItem> generateOrderLineItems(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItem> orderLineItems = orderCreateRequest.getOrderLineItems()
                .stream()
                .map(orderLineItemRequest -> new OrderLineItem(
                        orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity()
                )).collect(Collectors.toList());

        validateOrderLineItemsEmpty(orderLineItems);

        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        validateSizeAndMenuCountDifferent(orderLineItems, menuRepository.countByIdIn(menuIds));

        return orderLineItems;
    }

    private void validateOrderLineItemsEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSizeAndMenuCountDifferent(List<OrderLineItem> orderLineItems, long menuCount) {
        if (isSizeDifferentFromMenuCount(orderLineItems, menuCount)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isSizeDifferentFromMenuCount(List<OrderLineItem> orderLineItems, long menuCount) {
        return orderLineItems.size() != menuCount;
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream().map(OrderResponse::of).collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, OrderStatusChangeRequest orderStatusChangeRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);

        savedOrder.validateOrderStatusComplete();

        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusChangeRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        return OrderResponse.of(savedOrder);
    }
}

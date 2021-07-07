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
        OrderLineItems orderLineItems = generateOrderLineItems(orderCreateRequest);

        OrderTable orderTable = generateOrderTable(orderCreateRequest);

        Order savedOrder = generateOrder(orderTable, orderLineItems);

        return OrderResponse.of(savedOrder);
    }

    private OrderTable generateOrderTable(OrderCreateRequest orderCreateRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(NoSuchElementException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return orderTable;
    }

    private OrderLineItems generateOrderLineItems(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItem> orderLineItemList = orderCreateRequest.getOrderLineItems()
                .stream()
                .map(orderLineItemRequest -> new OrderLineItem(
                        orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity()
                )).collect(Collectors.toList());

        OrderLineItems orderLineItems = new OrderLineItems(orderLineItemList);

        orderLineItems.validateOrderLineItemsEmpty();

        List<Long> menuIds = orderLineItems.menuIds();

        orderLineItems.validateSizeAndMenuCountDifferent(menuRepository.countByIdIn(menuIds));

        return orderLineItems;
    }

    private Order generateOrder(OrderTable orderTable, OrderLineItems orderLineItems) {
        Order savedOrder = orderRepository.save(new Order(orderTable));
        savedOrder.setOrderStatus(OrderStatus.COOKING.name());
        savedOrder.setOrderedTime(LocalDateTime.now());

        for (OrderLineItem orderLineItem : orderLineItems.orderLineItems()) {
            orderLineItem.setOrder(savedOrder);
        }

        savedOrder.addOrderLineItems(orderLineItems);
        return savedOrder;
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
        savedOrder.setOrderStatus(orderStatus.name());

        return OrderResponse.of(savedOrder);
    }
}

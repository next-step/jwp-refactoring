package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Orders;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
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

    @Transactional
    public OrderResponse create(OrderRequest request) {
        final List<OrderLineItemRequest> orderLineItems = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Orders orders = new Orders();
        orders.setOrderTableId(orderTable.getId());
        orders.setOrderStatus(OrderStatus.COOKING.name());
        orders.setOrderedTime(LocalDateTime.now());

        final Orders savedOrders = orderRepository.save(orders);

        final Long orderId = savedOrders.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItems) {
            OrderLineItem saveOrderLineItem = new OrderLineItem();
            saveOrderLineItem.setOrderId(orderId);
            saveOrderLineItem.setMenuId(orderLineItemRequest.getMenuId());
            saveOrderLineItem.setQuantity(orderLineItemRequest.getQuantity());
            savedOrderLineItems.add(orderLineItemRepository.save(saveOrderLineItem));
        }
        savedOrders.setOrderLineItems(savedOrderLineItems);

        return OrderResponse.from(savedOrders);
    }

    public List<OrderResponse> list() {
        final List<Orders> orders = orderRepository.findAll();

        for (final Orders order : orders) {
            order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, OrderRequest request) {
        final Orders savedOrders = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrders.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrders.setOrderStatus(orderStatus.name());

        orderRepository.save(savedOrders);

        savedOrders.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return OrderResponse.from(savedOrders);
    }
}

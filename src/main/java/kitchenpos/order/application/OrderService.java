package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
        final MenuDao menuDao,
        final OrderDao orderDao,
        final OrderLineItemDao orderLineItemDao,
        final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest order) {
        final List<OrderLineItemRequest> orderLineItems = order.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order savedOrder = orderDao.save(order.toEntity(orderTable.getId(),
            OrderStatus.COOKING.name(), LocalDateTime.now()));

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemRequest orderLineItem : orderLineItems) {
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem.toEntity(orderId)));
        }
        savedOrder.changeOrderLineItems(savedOrderLineItems);

        return new OrderResponse(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.changeOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders.stream()
            .map(OrderResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.changeOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}

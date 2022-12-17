package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public OrderResponse create(final Order order) {

        validateOrderLineItem(order);

        long menuSize = menuDao.countByIdIn(getOrderLineIds(order));
        validateOrderLine(order, menuSize);

        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        order(order, orderTable);

        final Order savedOrder = orderDao.save(order);
        savedOrder.setOrderLineItems(saveOrderLine(order, savedOrder.getId()));

        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> saveOrderLine(Order order, Long orderId) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : order.getOrderLineItems()) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return savedOrderLineItems;
    }

    private void order(Order order, OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
    }

    private List<Long> getOrderLineIds(Order order) {
        return order.getOrderLineItems().stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
    }

    private void validateOrderLine(Order order, long menuSize) {
        if (order.getOrderLineItems().size() != menuSize) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItem(Order order) {
        if (CollectionUtils.isEmpty(order.getOrderLineItems())) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        changeOrderStatus(order, savedOrder);

        orderDao.save(savedOrder);
        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
        return OrderResponse.from(savedOrder);
    }

    private void changeOrderStatus(Order order, Order savedOrder) {
        validateStatus(savedOrder);
        savedOrder.setOrderStatus(order.getOrderStatus());
    }

    private void validateStatus(Order order) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), order.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
    }
}

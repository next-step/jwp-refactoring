package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Order create(final Order order) {
        OrderCreate orderCreate = new OrderCreate(
                order.getOrderTableId(),
                OrderStatus.valueOf(order.getOldOrderStatus()),
                order.getOrderLineItems()
                        .stream()
                        .map(item -> new OrderLineItemCreate(item.getOldMenuId(), item.getQuantity()))
                        .collect(Collectors.toList())
                );

        return create(orderCreate);
    }

    public Order create(OrderCreate orderCreate) {
        List<Long> menuIds = orderCreate.getOrderLineItems()
                .stream()
                .map(item -> item.getMenuId())
                .collect(Collectors.toList());

        Menus menus = new Menus(menuDao.findAllById(menuIds));
        OrderTable orderTable = orderTableDao.findById(orderCreate.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        Order.create(orderCreate, menus, orderTable);

        return null;
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        return changeOrderStatus(orderId, order.getOrderStatus());
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        order.changeOrderStatus(orderStatus);

        return order;
    }
}

package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menus;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(final MenuDao menuDao, final OrderDao orderDao,
        final OrderTableDao orderTableDao) {

        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.menuIds();
        final Menus menus = new Menus(menuDao.findAllByIdIn(menuIds));

        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        final OrderLineItems orderLineItems = makeOrderLineItems(orderRequest, menus);
        final Order order = new Order(orderTable, orderLineItems);

        return orderDao.save(order);
    }

    private OrderLineItems makeOrderLineItems(OrderRequest orderRequest, Menus menus) {
        final OrderLineItems orderLineItems = new OrderLineItems();
        orderRequest.getOrderLineItems()
            .forEach(it -> orderLineItems.add(new OrderLineItem(menus.get(it.getMenuId()), it.getQuantity())));

        return orderLineItems;
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findOrder(orderId);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
        order.changeStatus(orderStatus);

        return orderDao.save(order);
    }

    private Order findOrder(Long orderId) {
        return orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
    }
}

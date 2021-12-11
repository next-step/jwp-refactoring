package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
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
    public Order create(final OrderRequest request) {
        checkMenus(request);

        final OrderTable orderTable = getOrderTable(request.getOrderTableId());
        final List<OrderLineItem> orderLineItems = getOrderLineItems(request.getOrderLineItems());

        orderTable.checkEmpty();

        final Order order = Order.create(orderTable, OrderStatus.COOKING, orderLineItems);
        return orderDao.save(order);
    }

    private void checkMenus(OrderRequest request) {
        final List<Long> menuIds = request.getMenuIds();

        if (CollectionUtils.isEmpty(menuIds)) {
            throw new IllegalArgumentException();
        }

        if (menuIds.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> new OrderLineItem(getMenu(orderLineItemRequest.getMenuId()), orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private Menu getMenu(Long menuId) {
        return menuDao.findById(menuId).orElseThrow(IllegalArgumentException::new);
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = order.getOrderStatus();
        savedOrder.setOrderStatus(orderStatus);

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}

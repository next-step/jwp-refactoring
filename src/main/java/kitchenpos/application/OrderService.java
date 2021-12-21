package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public OrderResponse create(OrderRequest orderRequest) {
        OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        List<Menu> menus = menuDao.findAllById(orderRequest.getMenuIds());

        return OrderResponse.of(orderDao.save(orderRequest.toOrder(orderTable, menus)));
    }

    public List<OrderResponse> list() {
        return OrderResponse.toList(orderDao.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatusRequest.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }
}

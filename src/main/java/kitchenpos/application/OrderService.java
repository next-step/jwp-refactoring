package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.validate.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    private final OrderValidator validator;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderTableDao orderTableDao,
            final OrderValidator validator
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.validator = validator;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();
        final OrderTable orderTable = getOrderTable(orderRequest.getOrderTableId());

        validator.validateCreate(orderLineItemRequests, orderTable);

        Order order = OrderRequest.to(orderRequest, orderTable);
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = getMenu(orderLineItemRequest.getMenuId());
            order.addOrderLineItem(new OrderLineItem(menu.getId(), orderLineItemRequest.getQuantity()));
        }
        return orderDao.save(order);
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = getOrder(orderId);
        validator.validateChangeOrderStatus(savedOrder);

        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        return savedOrder;
    }

    private Order getOrder(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Menu getMenu(Long menuId) {
        return menuDao.findById(menuId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}

package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
        final MenuDao menuDao,
        final OrderDao orderDao,
        final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        OrderTable orderTable = findOrderTableById(request.getOrderTableId());
        List<OrderLineItem> orderLineItems = getOrderLineItems(request.getOrderLineItems());
        Order order = request.toOrder(orderTable, orderLineItems);
        order.order();
        return OrderResponse.from(orderDao.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order order = findOrderById(orderId);
        order.changeOrderStatus(orderStatus);
        return OrderResponse.from(orderDao.save(order));
    }

    private Order findOrderById(Long orderId) {
        return orderDao.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("주문 정보가 없습니다"));
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("주문 테이블 정보가 없습니다."));
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> requests) {
        return requests.stream()
            .map(request -> request.toOrderLineItem(findMenuById(request.getMenuId())))
            .collect(Collectors.toList());
    }

    private Menu findMenuById(Long id) {
        return menuDao.findById(id).orElseThrow(() -> new IllegalArgumentException("메뉴 정보가 없습니다."));
    }
}

package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
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
    public OrderResponse create(final OrderRequest request) {
        Order order = request.toOrder(getOrderTable(request.getOrderTableId()), getOrderLineItems(request.getOrderLineItems()));
        order(order);
        final Order savedOrder = orderDao.save(order);

        return OrderResponse.from(savedOrder);
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> requests){
        if(Objects.isNull(requests) || requests.isEmpty()){
            throw new IllegalArgumentException("주문 항목 정보 없음");
        }
        return requests.stream()
            .map(request -> request.toOrderLineItem(findMenuById(request.getMenuId())))
            .collect(Collectors.toList());
    }

    private Menu findMenuById(Long id){
        return menuDao.findById(id).orElseThrow(() -> new IllegalArgumentException("메뉴 정보가 없습니다."));
    }

    private void order(Order order) {
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
    }

    private List<Long> getOrderLineIds(OrderRequest request) {
        return request.getOrderLineItems().stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
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
    public OrderResponse changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        changeOrderStatus(savedOrder, orderStatus);

        orderDao.save(savedOrder);
        return OrderResponse.from(savedOrder);
    }

    private void changeOrderStatus(Order savedOrder, String orderStatus) {
        savedOrder.setOrderStatus(orderStatus);
    }
}

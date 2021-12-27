package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderResponses;
import kitchenpos.exception.KitchenposNotFoundException;

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
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderLineItems orderLineItems = makeOrderLineItems(orderRequest);

        OrderTable orderTable = makeOrderTable(orderRequest);

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        return OrderResponse.from(orderDao.save(order));
    }

    private OrderLineItems makeOrderLineItems(OrderRequest orderRequest) {
        List<OrderLineItem> items = orderRequest.getOrderLineItems().stream()
            .map(orderLineItemRequest -> {
                Menu menu = menuDao.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(KitchenposNotFoundException::new);
                return new OrderLineItem(menu, orderLineItemRequest.getQuantity());
            })
            .collect(Collectors.toList());

        OrderLineItems orderLineItems = new OrderLineItems(items);

        final List<Long> menuIds = orderLineItems.getIds();
        orderLineItems.validateSize(menuDao.countByIdIn(menuIds));

        return orderLineItems;
    }

    private OrderTable makeOrderTable(OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
            .orElseThrow(KitchenposNotFoundException::new);
        orderTable.checkNotEmpty();
        return orderTable;
    }

    public OrderResponses list() {
        final List<Order> orders = orderDao.findAll();

        return OrderResponses.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(KitchenposNotFoundException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.updateOrderStatus(orderStatus);

        orderDao.save(savedOrder);

        savedOrder.addOrderLineItems(orderLineItemDao.findAllByOrder_Id(orderId));

        return OrderResponse.from(savedOrder);
    }
}

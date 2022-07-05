package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItemDao;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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

    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        Order order = new Order(orderTable);
        setOrderLineItems(order, orderRequest);

        return OrderResponse.from(orderDao.save(order));
    }

    public List<Order> list() {
//        final List<Order> orders = orderDao.findAll();
//
//        for (final Order order : orders) {
//            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
//        }
//
//        return orders;
        return null;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
//        final Order savedOrder = orderDao.findById(orderId)
//                .orElseThrow(IllegalArgumentException::new);
//
//        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
//            throw new IllegalArgumentException();
//        }
//
//        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
//        savedOrder.setOrderStatus(orderStatus.name());
//
//        orderDao.save(savedOrder);
//
//        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
//
//        return savedOrder;
        return null;
    }

    private void setOrderLineItems(Order order, OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequest();

        validateOrderLineItemRequestEmpty(orderLineItemRequests);
        validateIfThereIsMenu(orderRequest.getMenuIds());

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for(OrderLineItemRequest orderLineItemRequest: orderLineItemRequests) {
            final Menu menu = findMenu(orderLineItemRequest.getMenuId());

            orderLineItems.add(new OrderLineItem(order, menu, orderLineItemRequest.getQuantity()));
        }
    }

    private void validateOrderLineItemRequestEmpty(List<OrderLineItemRequest> orderLineItemRequests) {
        if(CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIfThereIsMenu(List<Long> menuIds) {
        if(menuIds.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private Menu findMenu(Long menuId) {
        return menuDao.findById(menuId)
            .orElseThrow(IllegalArgumentException::new);
    }
}

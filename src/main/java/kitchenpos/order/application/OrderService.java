package kitchenpos.order.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.common.domian.Quantity;
import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.order.repository.OrderLineItemDao;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableDao;

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
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        final List<Menu> menus = menuDao.findAllById(menuIds);

        if (orderLineItemRequests.size() != menus.size()) {
            throw new CustomException(ErrorInfo.NOT_FOUND_MENU);
        }

        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new CustomException(ErrorInfo.NOT_FOUND_ORDER_TABLE));

        Order order = Order.of(orderTable.getId());

        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu findMenu = menus.stream()
                    .filter(menu -> menu.getId().equals(orderLineItemRequest.getMenuId()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(ErrorInfo.NOT_FOUND_MENU));

            OrderLineItem orderLineItem = OrderLineItem.of(order, findMenu, new Quantity(orderLineItemRequest.getQuantity()));
            orderLineItemDao.save(orderLineItem);
        }

        return orderDao.save(order).toResponse();
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = order.getOrderStatus();
        savedOrder.changeOrderStatus(orderStatus);

        orderDao.save(savedOrder);

//        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}

package kitchenpos.order.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kitchenpos.menugroup.repository.MenuGroupDao;
import kitchenpos.order.dto.OrderStatusRequest;
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
@Transactional
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;
    private final MenuGroupDao menuGroupDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao,
            final MenuGroupDao menuGroupDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
        this.menuGroupDao = menuGroupDao;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new CustomException(ErrorInfo.NOT_FOUND_ORDER_LINE_REQUEST);
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

        Order order = Order.of(orderTable.getId(), orderLineItemRequests, menus);
        return OrderResponse.of(orderDao.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderDao.findAll());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorInfo.NOT_FOUND_ORDER));

        order.checkAlreadyComplete();
        order.changeOrderStatus(OrderStatus.get(orderStatusRequest.getOrderStatus()));
        return OrderResponse.of(order);
    }
}

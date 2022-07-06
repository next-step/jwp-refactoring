package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuService menuService;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuService menuService,
            final OrderDao orderDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuService = menuService;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        validateIfThereIsMenu(orderRequest.toMenuIds());

        List<OrderLineItem> orderLineItems = convertOrderLineItems(orderRequest);
        Order order = new Order(orderTable, orderLineItems);

        return OrderResponse.from(orderDao.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        return orders.stream()
            .map(OrderResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.updateOrderStatus(orderStatusRequest.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> convertOrderLineItems(OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for(OrderLineItemRequest orderLineItemRequest: orderLineItemRequests) {
            final Menu menu = menuService.findMenuById(orderLineItemRequest.getMenuId());

            orderLineItems.add(new OrderLineItem(menu, orderLineItemRequest.getQuantity()));
        }

        return orderLineItems;
    }

    private void validateIfThereIsMenu(List<Long> menuIds) {
        if(menuIds.size() != menuService.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }
}

package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Quantity;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderDao orderDao;
    private final MenuService menuService;
    private final TableService tableService;

    public OrderService(
            OrderDao orderDao
            , MenuService menuService
            , TableService tableService) {
        this.orderDao = orderDao;
        this.menuService = menuService;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        OrderTable orderTable = tableService.findById(request.getOrderTableId());
        OrderLineItems orderLineItems = OrderLineItems.of(getOrderLineItems(request.getOrderLineItems()));

        Order order = Order.of(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        Order persistOrder = orderDao.save(order);
        return OrderResponse.of(persistOrder);
    }

    public List<OrderResponse> list() {
        List<Order> persistOrders = orderDao.findAll();

        return persistOrders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        Order persistOrder = findById(orderId);
        OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());

        persistOrder.changeOrderStatus(orderStatus);
        return OrderResponse.of(persistOrder);
    }

    public Order findById(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
    }

    public boolean isCookingOrMealExists(OrderTables orderTables) {
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        return orderDao.existsByOrderTableInAndOrderStatusIn(orderTables.getOrderTables(), orderStatuses);
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest ->
                {
                    Menu menu = menuService.findById(orderLineItemRequest.getMenuId());
                    Quantity quantity = Quantity.of(orderLineItemRequest.getQuantity());
                    return OrderLineItem.of(menu, quantity);
                })
                .collect(Collectors.toList());
    }
}

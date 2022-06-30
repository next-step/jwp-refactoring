package kitchenpos.order.application;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.*;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static kitchenpos.common.Messages.ORDER_LINE_ITEM_REQUIRED;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final TableService tableService;

    public OrderService(OrderRepository orderRepository, MenuService menuService, TableService tableService) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = tableService.findById(orderRequest.getOrderTableId());
        validateOrderLineItems(orderRequest.getOrderLineItems());

        List<OrderLineItem> requestOrderLineItems = toOrderLineItems(orderRequest.getOrderLineItems());
        OrderLineItems orderLineItems = OrderLineItems.of(requestOrderLineItems);
        orderLineItems.validateOrderLineItems(requestOrderLineItems);

        Order order = Order.of(orderTable, orderLineItems);
        return OrderResponse.of(orderRepository.save(order));
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequest) {
        if (CollectionUtils.isEmpty(orderLineItemRequest)) {
            throw new IllegalArgumentException(ORDER_LINE_ITEM_REQUIRED);
        }
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public ChangeOrderStatusResponse changeOrderStatus(
            final Long orderId,
            final ChangeOrderStatusRequest changeOrderStatusRequest
    ) {
        Order order = orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
        OrderStatus orderStatus = OrderStatus.valueOf(changeOrderStatusRequest.getOrderStatus());
        order.changeOrderStatus(orderStatus);

        return ChangeOrderStatusResponse.of(orderRepository.save(order));
    }

    public boolean isOrderTableStateCookingOrMeal(OrderTable orderTable) {
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        return orderRepository.existsByOrderTableAndOrderStatusIn(orderTable, orderStatuses);
    }

    public boolean isOrderTablesStateInCookingOrMeal(List<OrderTable> orderTables) {
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        return orderRepository.existsByOrderTableInAndOrderStatusIn(orderTables, orderStatuses);
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream().map(orderLineItemRequest -> {
            Menu menu = menuService.findById(orderLineItemRequest.getMenuId());
            Quantity quantity = Quantity.of(orderLineItemRequest.getQuantity());
            return OrderLineItem.of(menu, quantity);
        }).collect(Collectors.toList());
    }
}

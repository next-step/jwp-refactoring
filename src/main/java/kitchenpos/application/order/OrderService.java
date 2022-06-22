package kitchenpos.application.order;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.table.OrderTableService;
import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.exception.NotEqualsMenuAndOrderLineItemMenuException;
import kitchenpos.exception.NotExistOrderLineItemsException;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.OrderTableAlreadyEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuService menuService;
    private final OrderTableService orderTableService;
    private final OrderRepository orderRepository;

    public OrderService(
            final MenuService menuService,
            final OrderTableService orderTableService,
            final OrderRepository orderRepository
    ) {
        this.menuService = menuService;
        this.orderTableService = orderTableService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderTableService.findOrderTable(orderRequest.getOrderTableId());
        validateCreateOrder(orderTable, orderRequest.getOrderLineItems());

        Order order = Order.from(orderTable);
        List<OrderLineItem> orderLineItems = this.findOrderLineItems(orderRequest.getOrderLineItems());
        order.addAllOrderLineItems(orderLineItems);

        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return OrderResponse.fromList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order order = this.findOrder(orderId);
        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> findOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        List<OrderLineItem> result = Lists.newArrayList();
        for (OrderLineItemRequest orderLineItem : orderLineItems) {
            Menu menu = menuService.findMenu(orderLineItem.getMenuId());
            result.add(OrderLineItem.of(menu, orderLineItem.getQuantity()));
        }
        return result;
    }

    private void validateCreateOrder(OrderTable orderTable, List<OrderLineItemRequest> orderLineItems) {
        validateEmptyOrderTable(orderTable);
        validateOrderLineItems(orderLineItems);
        validateExistMenu(orderLineItems);
    }

    private void validateExistMenu(List<OrderLineItemRequest> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream().map(OrderLineItemRequest::getMenuId).collect(Collectors.toList());
        long menuCount = menuService.countByIdIn(menuIds);

        if (menuCount != menuIds.size()) {
            throw new NotEqualsMenuAndOrderLineItemMenuException(menuCount, menuIds.size());
        }
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new NotExistOrderLineItemsException();
        }
    }

    private void validateEmptyOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new OrderTableAlreadyEmptyException(orderTable.getId());
        }
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundOrderException(orderId));
    }

    public boolean isExistDontUnGroupState(List<OrderTable> orderTables) {
        return orderRepository.existsByOrderTableInAndOrderStatusIn(
                orderTables,
                OrderStatus.dontUngroupStatus()
        );
    }

    public boolean isExistDontUnGroupState(OrderTable orderTable) {
        return orderRepository.existsByOrderTableAndOrderStatusIn(
                orderTable,
                OrderStatus.dontUngroupStatus()
        );
    }
}

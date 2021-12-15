package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrdersRepository;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrdersRequest;
import kitchenpos.dto.order.OrdersResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.utils.StreamUtils;

@Service
public class OrdersService {
    private final MenuRepository menuRepository;
    private final OrdersRepository ordersRepository;
    private final OrderTableRepository orderTableRepository;

    public OrdersService(final MenuRepository menuRepository,
                         final OrdersRepository ordersRepository,
                         final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.ordersRepository = ordersRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrdersResponse create(final OrdersRequest ordersRequest) {
        OrderTable orderTable = findOrderTable(ordersRequest.getOrderTableId());
        validateCreateOrder(orderTable, ordersRequest.getOrderLineItems());

        Orders orders = Orders.from(orderTable);
        List<OrderLineItem> orderLineItems = createOrderLineItems(ordersRequest);
        orders.addOrderLineItems(orderLineItems);

        return OrdersResponse.from(ordersRepository.save(orders));
    }

    private void validateCreateOrder(OrderTable orderTable, List<OrderLineItemRequest> orderLineItems) {
        validateIsEmptyOrderTable(orderTable);
        validateIsEmptyOrderLineItems(orderLineItems);
        validateExistMenus(orderLineItems);
    }

    @Transactional(readOnly = true)
    public List<OrdersResponse> list() {
        final List<Orders> orders = ordersRepository.findAll();
        return StreamUtils.mapToList(orders, OrdersResponse::from);
    }

    @Transactional
    public OrdersResponse changeOrderStatus(final Long orderId, final OrdersRequest ordersRequest) {
        Orders orders = findOrders(orderId);
        orders.changeOrderStatus(ordersRequest.getOrderStatus());

        return OrdersResponse.from(orders);
    }

    private List<OrderLineItem> createOrderLineItems(OrdersRequest ordersRequest) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : ordersRequest.getOrderLineItems()) {
            Menu menu = findMenu(orderLineItemRequest.getMenuId());
            OrderLineItem orderLineItem = OrderLineItem.of(menu, orderLineItemRequest.getQuantity());

            orderLineItems.add(orderLineItem);
        }

        return orderLineItems;
    }

    private Orders findOrders(Long orderId) {
        return ordersRepository.findById(orderId)
                               .orElseThrow(IllegalArgumentException::new);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                                   .orElseThrow(IllegalArgumentException::new);
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                             .orElseThrow(IllegalArgumentException::new);
    }

    private void validateIsEmptyOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIsEmptyOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExistMenus(List<OrderLineItemRequest> orderLineItems) {
        List<Long> menuIds = StreamUtils.mapToList(orderLineItems, OrderLineItemRequest::getMenuId);
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }
}

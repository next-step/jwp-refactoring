package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.utils.StreamUtils;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        validateCreateOrder(orderTable, orderRequest.getOrderLineItems());

        Order order = Order.from(orderTable);
        List<OrderLineItem> orderLineItems = createOrderLineItems(orderRequest);
        order.addOrderLineItems(orderLineItems);

        return OrderResponse.from(orderRepository.save(order));
    }

    private void validateCreateOrder(OrderTable orderTable, List<OrderLineItemRequest> orderLineItems) {
        validateIsEmptyOrderTable(orderTable);
        validateIsEmptyOrderLineItems(orderLineItems);
        validateExistMenus(orderLineItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> order = orderRepository.findAll();
        return StreamUtils.mapToList(order, OrderResponse::from);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order order = findOrders(orderId);
        order.changeOrderStatus(orderRequest.getOrderStatus());

        return OrderResponse.from(order);
    }

    private List<OrderLineItem> createOrderLineItems(OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            Menu menu = findMenu(orderLineItemRequest.getMenuId());
            OrderLineItem orderLineItem = OrderLineItem.of(menu, orderLineItemRequest.getQuantity());

            orderLineItems.add(orderLineItem);
        }

        return orderLineItems;
    }

    private Order findOrders(Long orderId) {
        return orderRepository.findById(orderId)
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

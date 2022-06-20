package kitchenpos.application;

import com.google.common.collect.Lists;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = this.findOrderTable(orderRequest.getOrderTableId());
        validateCreateOrder(orderTable, orderRequest.getOrderLineItems());

        Order order = Order.from(orderTable);
        List<OrderLineItem> orderLineItems = this.findOrderLineItems(orderRequest.getOrderLineItems());
        order.addAllOrderLineItems(orderLineItems);

        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
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
            Menu menu = this.findMenu(orderLineItem.getMenuId());
            result.add(OrderLineItem.of(menu.getId(), orderLineItem.getQuantity()));
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
        long menuCount = menuRepository.countByIdIn(menuIds);

        if (menuCount != menuIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmptyOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(IllegalArgumentException::new);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
    }
}

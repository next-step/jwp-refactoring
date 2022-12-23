package kitchenpos.order.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.constant.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderLineItems orderLineItems = new OrderLineItems();
        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            Menu menu = getMenu(orderLineItemRequest.getMenuId());

            OrderLineItem orderLineItem = OrderLineItem.create(menu, orderLineItemRequest.getQuantity());
            OrderLineItem createdOrderLineItem = orderLineItemRepository.save(orderLineItem);

            orderLineItems.add(createdOrderLineItem);
        }

        OrderTable orderTable = getOrderTable(orderRequest.getOrderTableId());

        Order order = Order.create(
                orderTable, OrderStatus.COOKING.name(),
                LocalDateTime.now(), orderLineItems,
                orderRequest, menuRepository.countByIdIn(orderRequest.getMenuIds()));
        Order createdOrder = orderRepository.save(order);

        return OrderResponse.from(createdOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAllJoinFetch();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        Order order = getOrder(orderId);
        order.changeStatus(orderStatusRequest.getOrderStatus());
        Order createdOrder = orderRepository.save(order);
        return OrderResponse.from(createdOrder);
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(() -> new EntityNotFoundException("OrderTable", orderTableId));
    }

    private Menu getMenu(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("Menu", menuId));
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order", orderId));
    }

}

package kitchenpos.order.application;

import kitchenpos.exception.CannotFindException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.common.Message.*;
import static kitchenpos.order.domain.OrderStatus.COOKING;

@Transactional
@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = findOrderTable(orderRequest);
        final OrderLineItems orderLineItems = getOrderLineItems(orderRequest);
        final Order savedOrder = orderRepository.save(new Order(orderTable, COOKING, orderLineItems));

        return OrderResponse.of(savedOrder);
    }

    private OrderLineItems getOrderLineItems(OrderRequest orderRequest) {
        return new OrderLineItems(orderRequest.getOrderLineItemRequests()
                .stream()
                .map(this::getOrderLineItem)
                .collect(Collectors.toList()));
    }

    private OrderLineItem getOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = findMenuById(orderLineItemRequest.getMenuId());
        return new OrderLineItem(menu, Quantity.of(orderLineItemRequest.getQuantity()));
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new CannotFindException(ERROR_ORDER_SHOULD_HAVE_REGISTERED_MENU));
    }

    private OrderTable findOrderTable(OrderRequest orderRequest) {
        return orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new CannotFindException(ERROR_ORDER_SHOULD_HAVE_REGISTERED_TABLE));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.ofList(orders);
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = findOrderById(orderId);
        return OrderResponse.of(savedOrder.changeOrderStatus(orderStatusRequest.getOrderStatus()));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CannotFindException(ERROR_ORDER_NOT_FOUND));
    }
}

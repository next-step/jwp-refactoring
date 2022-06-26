package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuService menuService,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();
        List<OrderLineItem> orderLineItems = findOrderLineItems(orderLineItemRequests);
        OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        Order order = orderRequest.toOrder(orderTable, OrderLineItems.from(orderLineItems), orderLineItemRequests.size());
        return OrderResponse.from(orderRepository.save(order));
    }

    private List<OrderLineItem> findOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menuService.findMenu(orderLineItemRequest.getMenuId());
            orderLineItems.add(OrderLineItem.from(menu, orderLineItemRequest.getQuantity()));
        }
        return orderLineItems;
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문테이블을 찾을 수 없습니다."));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = findOrder(orderId);
        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        Order order = orderRepository.save(savedOrder);
        return OrderResponse.from(order);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
    }

    public void validateComplete(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableAndOrderStatusIn(orderTable,
                Arrays.asList(COOKING, MEAL))) {
            throw new IllegalArgumentException("주문테이블의 주문이 완료상태가 아닙니다.");
        }
    }

    public void validateComplete(List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                orderTables, Arrays.asList(COOKING, MEAL))) {
            throw new IllegalArgumentException("주문테이블들의 주문이 완료상태가 아닙니다.");
        }
    }
}

package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        OrderTable orderTable = findOrderTableById(request.getOrderTableId());
        OrderLineItems orderLineItems = findOrderLineItems(request.getOrderLineItems());
        Order savedOrder = orderRepository.save(Order.of(orderTable, orderLineItems));
        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAll();
        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final String orderStatus) {
        Order order = findOrderById(orderId);
        order.changeOrderStatus(OrderStatus.valueOf(orderStatus));
        return OrderResponse.of(order);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 없습니다."));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));
    }

    private OrderLineItems findOrderLineItems(List<OrderLineItemResponse> OrderLineItemResponses) {
        List<OrderLineItem> orderLineItemList = OrderLineItemResponses.stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toList());
        return new OrderLineItems(orderLineItemList);
    }

    private OrderLineItem toOrderLineItem(OrderLineItemResponse orderLineItemResponse) {
        Menu menu = menuRepository.findById(orderLineItemResponse.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
        return new OrderLineItem(menu.getId(), orderLineItemResponse.getQuantity());
    }
}

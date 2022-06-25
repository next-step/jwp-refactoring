package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository,
                        MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = orderRequest.toOrder();
        OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());

        OrderLineItems orderLineItems = new OrderLineItems();
        addOrderLineItems(orderRequest, orderLineItems);
        order.registerOrderLineItems(orderLineItems, orderRequest.getOrderLineItems().size());
        orderTable.registerOrder(order);

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    private void addOrderLineItems(OrderRequest orderRequest, OrderLineItems orderLineItems) {
        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            OrderLineItem orderLineItem = orderLineItemRequest.toOrderLineItem();
            Menu menu = findMenu(orderLineItemRequest.getMenuId());

            orderLineItem.setMenu(menu);
            orderLineItems.addOrderLineItems(orderLineItem);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findOrder(orderId);
        order.changeOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()));
        return OrderResponse.from(order);
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 메뉴가 등록되어있지 않습니다."));
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 주문이 등록되어있지 않습니다."));
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테이블이 등록되어있지 않습니다."));
    }

}

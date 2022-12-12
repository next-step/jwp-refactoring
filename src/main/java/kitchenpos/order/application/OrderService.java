package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
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

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Set<Menu> menuSet = new HashSet<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId()).orElseThrow(IllegalArgumentException::new);
            orderLineItems.add(new OrderLineItem.Builder()
                    .menu(menu)
                    .quantity(orderLineItemRequest.getQuantity())
                    .build());
            menuSet.add(menu);
        }
        validateOrderLineItemsSizeEqualsMenuSize(orderLineItems, menuSet);
        return orderLineItems;
    }

    private void validateOrderLineItemsSizeEqualsMenuSize(List<OrderLineItem> orderLineItems, Set<Menu> menuSet) {
        if (orderLineItems.size() != menuSet.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = toOrderLineItems(orderRequest.getOrderLineItemRequests());
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(IllegalArgumentException::new);

        Order order = new Order.Builder()
                .orderTable(orderTable)
                .orderLineItems(orderLineItems)
                .build();
        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderRequest orderRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(order);
    }
}

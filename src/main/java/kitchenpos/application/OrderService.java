package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository,
                        OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = toOrderLineItems(request);
        OrderTable orderTable = findOrderTableById(request);
        Order order = Order.of(orderTable, orderLineItems);
        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> toOrderLineItems(OrderRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = findMenuById(orderLineItemRequest.getMenuId());
        return OrderLineItem.of(menu.getId(), orderLineItemRequest.getQuantity());
    }

    private Menu findMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    private OrderTable findOrderTableById(OrderRequest request) {
        return orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
    }
}

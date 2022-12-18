package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
    public OrderResponse createOrder(final OrderCreateRequest request) {
        Order order = mapToOrder(request);
        return OrderResponse.of(orderRepository.save(order));
    }

    private Order mapToOrder(OrderCreateRequest request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(EntityNotFoundException::new);
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(request.getOrderLineItems());
        return Order.cooking(orderTable, orderLineItems);
    }

    private List<OrderLineItem> mapToOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(this::mapToOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem mapToOrderLineItem(OrderLineItemRequest item) {
        Menu menu = menuRepository.findById(item.getMenuId())
                .orElseThrow(EntityNotFoundException::new);
        return OrderLineItem.of(menu, item.getQuantity());
    }

    public List<OrderResponse> findAllOrders() {
        final List<Order> orders = orderRepository.findAllWithLineItems();
        return OrderResponse.list(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order order = orderRepository.findWithLineItemById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.changeState(orderStatus);
        return OrderResponse.of(order);
    }
}

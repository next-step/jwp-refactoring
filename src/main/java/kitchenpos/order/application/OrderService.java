package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
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
    private final OrderValidator orderValidator;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderValidator orderValidator
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse createOrder(final OrderCreateRequest request) {
        orderValidator.validateOrderTable(request.getOrderTableId());
        Order order = toOrder(request);
        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> findAllOrders() {
        final List<Order> orders = orderRepository.findAllWithLineItems();
        return OrderResponse.list(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.changeState(orderStatus);
        return OrderResponse.of(order);
    }

    private Order toOrder(OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(request.getOrderLineItems());
        return Order.cooking(request.getOrderTableId(), orderLineItems);
    }

    private List<OrderLineItem> mapToOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(this::mapToOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem mapToOrderLineItem(OrderLineItemRequest item) {
        Menu menu = menuRepository.findById(item.getMenuId())
                .orElseThrow(EntityNotFoundException::new);
        return OrderLineItem.of(menu.getId(), item.getQuantity());
    }
}

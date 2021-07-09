package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
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

    public OrderResponse create(final OrderRequest orderRequest) {
        OrderLineItems orderLineItems = new OrderLineItems(orderRequest.getOrderLineItems());
        orderLineItems.checkInitOrderLineItems(countMenuSize(orderLineItems));

        checkOrderTableEmpty(orderRequest.getOrderTableId());

        Order order = new Order(orderRequest.getOrderTableId(),
                OrderStatus.COOKING.name(), LocalDateTime.now(), orderRequest.getOrderLineItems());
        return OrderResponse.from(orderRepository.save(order));
    }

    public void checkOrderTableEmpty (Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private int countMenuSize(OrderLineItems orderLineItems) {
        return menuRepository.findAllById(orderLineItems.toMenuIds()).size();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order findedOrder = findOrder(orderId);

        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        findedOrder.changeOrderStatus(orderStatus.name());

        return OrderResponse.from(findedOrder);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}

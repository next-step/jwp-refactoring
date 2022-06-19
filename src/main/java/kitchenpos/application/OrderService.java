package kitchenpos.application;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        List<OrderLineItem> orderLineItems = orderLineItemRequests.stream().map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());

        Order order = Order.of(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, order.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        order.changeOrderStatus(orderRequest.getOrderStatus());
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }
}

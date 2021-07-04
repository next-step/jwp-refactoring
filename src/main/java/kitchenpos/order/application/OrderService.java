package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = orderRequest.toEntity();
        order.validateOrderLineItemsSize(menuRepository.countByIdIn(order.menuIds()));

        final OrderTable persistOrderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        persistOrderTable.isValidForOrdering();
        order.isFrom(persistOrderTable);

        final Order persistOrder = orderRepository.save(order);

        return OrderResponse.of(persistOrder);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderRepository.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return savedOrder;
    }
}

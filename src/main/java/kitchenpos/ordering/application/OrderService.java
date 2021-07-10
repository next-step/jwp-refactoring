package kitchenpos.ordering.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.ordering.domain.OrderRepository;
import kitchenpos.ordering.domain.OrderStatus;
import kitchenpos.ordering.domain.Ordering;
import kitchenpos.ordering.dto.OrderRequest;
import kitchenpos.ordering.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Ordering order = orderRequest.toEntity();
        order.validateOrderLineItemsSize(menuRepository.countByIdIn(order.menuIds()));

        final OrderTable persistOrderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        order.calledFrom(persistOrderTable);

        return OrderResponse.of(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Ordering savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatusTo(orderStatus);

        return OrderResponse.of(savedOrder);
    }
}

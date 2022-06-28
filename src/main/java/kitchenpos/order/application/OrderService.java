package kitchenpos.order.application;

import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderResponse create(final OrderRequest request) {
        request.validate(menuRepository.countByIdIn(request.getMenuIds()));
        validateOrderTableExistsAndTableEmpty(request);

        Order order = request.toOrder();
        order.changeOrderStatus(OrderStatus.COOKING);
        order.changeOrderedTimeToCurrentTime();
        final Order persistOrder = orderRepository.save(order);
        orderRepository.flush();
        return OrderResponse.of(persistOrder);
    }

    private void validateOrderTableExistsAndTableEmpty(OrderRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order persistOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        persistOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.of(persistOrder);
    }
}

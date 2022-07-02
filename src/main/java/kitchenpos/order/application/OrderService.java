package kitchenpos.order.application;

import kitchenpos.order.dto.OrderLineItemRequest;
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
        validateOrderTableExistsAndTableEmpty(request);
        validate(request.getOrderLineItems());

        Order order = Order.of(request);
        order.changeOrderStatus(OrderStatus.COOKING);

        final Order persistOrder = orderRepository.save(order);
        return OrderResponse.of(persistOrder);
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

    public OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean existsOrderByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }

    private void validateOrderTableExistsAndTableEmpty(OrderRequest request) {
        final OrderTable orderTable = findOrderTableById(request.getOrderTableId());

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validate(List<OrderLineItemRequest> orderLineItems) {
        List<Long> menuIds = getMenuIds(orderLineItems);
        long menuCount = menuRepository.countByIdIn(menuIds);
        if (menuIds.size() != menuCount) {
            throw new IllegalArgumentException();
        }

    }

    private List<Long> getMenuIds(List<OrderLineItemRequest> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        return menuIds;
    }
}

package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final MenuRepository menuRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        validateOrderLineItems(request.getOrderLineItems());

        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문테이블을 찾을 수가 없습니다."));

        Order savedOrder = orderRepository.save(Order.of(orderTable, request.getOrderLineItems()));

        return OrderResponse.from(savedOrder);
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if(Objects.isNull(orderLineItems)) {
            throw new IllegalArgumentException("요청정보에 orderLineItems가 존재하지 않습니다.");
        }

        final List<Long> menuIds = mapToMenuIds(orderLineItems);

        if (orderLineItems.size() != countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private Long countByIdIn(final List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

    private List<Long> mapToMenuIds(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(order.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}

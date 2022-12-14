package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
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
        Long orderTableId = request.getOrderTableId();
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(orderTableId + "에 해당하는 주문테이블을 찾을 수가 없습니다."));
        List<OrderLineItemRequest> requestOrderLineItems = request.getOrderLineItemsRequest();
        validateOrderLineItems(requestOrderLineItems);
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(requestOrderLineItems);
        Order savedOrder = orderRepository.save(Order.of(orderTable, orderLineItems));

        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> mapToOrderLineItems(final List<OrderLineItemRequest> requestOrderLineItems) {
        return requestOrderLineItems.stream()
                .map(orderLineItem -> OrderLineItem.of(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }

    private void validateOrderLineItems(final List<OrderLineItemRequest> orderLineItems) {
        if (Objects.isNull(orderLineItems) || orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("요청정보에 주문정보가 존재하지 않습니다.");
        }

        final List<Long> menuIds = mapToMenuIds(orderLineItems);

        if (orderLineItems.size() != countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문정보 중 존재하지 않는 메뉴 정보가 있습니다.");
        }
    }

    private Long countByIdIn(final List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

    private List<Long> mapToMenuIds(final List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
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
                .orElseThrow(() -> new IllegalArgumentException(orderId + "에 대한 주문을 찾을 수 없습니다."));

        savedOrder.changeOrderStatus(order.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}

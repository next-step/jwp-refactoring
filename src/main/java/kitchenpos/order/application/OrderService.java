package kitchenpos.order.application;

import kitchenpos.common.ErrorMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
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
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND_ORDER_TABLE.getMessage(), orderTableId)));
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
            throw new IllegalArgumentException(ErrorMessage.INVALID_ORDER_LINE_ITEM.getMessage());
        }

        final List<Long> menuIds = fetchMenuIdsFrom(orderLineItems);

        if (orderLineItems.size() != countByIdIn(menuIds)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_MENU_INFO.getMessage());
        }
    }

    private Long countByIdIn(final List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

    private List<Long> fetchMenuIdsFrom(final List<OrderLineItemRequest> orderLineItems) {
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
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND_ORDER.getMessage(), orderId)));

        savedOrder.changeOrderStatus(order.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}

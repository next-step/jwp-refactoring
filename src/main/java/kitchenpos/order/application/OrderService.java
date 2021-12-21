package kitchenpos.order.application;

import kitchenpos.common.exception.NotChangeCompletionOrderException;
import kitchenpos.common.exception.NotOrderedEmptyTableException;
import kitchenpos.common.exception.OrderLineItemEmptyException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final MenuRepository menuRepository,
            final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = getCookingOrder(orderRequest);
        OrderLineItems orderLineItems = getOrderLineItems(orderRequest.getOrderLineItems());
        orderLineItems.initOrder(order);

        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    private Order getCookingOrder(OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findByIdElseThrow(orderRequest.getOrderTableId());
        return Order.CookingOrder(orderTable);
    }

    private OrderLineItems getOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new OrderLineItemEmptyException();
        }

        return new OrderLineItems(orderLineItemRequests.stream()
                .map(this::getOrderLineItem)
                .collect(Collectors.toList()));
    }

    private OrderLineItem getOrderLineItem(OrderLineItemRequest request) {
        Menu menu = menuRepository.findByIdElseThrow(request.getMenuId());
        return new OrderLineItem(menu, request.getQuantity());
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findByIdElseThrow(orderId);

        savedOrder.changeOrderStatus(orderStatusRequest.toOrderStatus());

        return OrderResponse.of(savedOrder);
    }
}

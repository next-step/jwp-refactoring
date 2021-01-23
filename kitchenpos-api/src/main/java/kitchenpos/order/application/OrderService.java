package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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

    public OrderResponse create(OrderRequest order) {
        List<OrderLineItemRequest> orderLineItemRequests = order.getOrderLineItems();
        List<OrderLineItem> orderLineItems = findOrderLineItems(orderLineItemRequests);

        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        Order savedOrder = orderRepository.save(new Order(orderTable, OrderStatus.COOKING));
        savedOrder.add(orderLineItems);
        return OrderResponse.of(savedOrder);
    }

    private List<OrderLineItem> findOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        validateOrderLineItemsSize(orderLineItemRequests);
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest request : orderLineItemRequests) {
            Menu menu = menuRepository.findById(request.getMenuId())
                    .orElseThrow(EntityNotFoundException::new);
            orderLineItems.add(new OrderLineItem(menu, request.getQuantity()));
        }

        return orderLineItems;
    }

    private void validateOrderLineItemsSize(List<OrderLineItemRequest> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(order.getOrderStatus()));
        return OrderResponse.of(savedOrder);
    }

}

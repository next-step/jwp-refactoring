package kitchenpos.application;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderLineItem.OrderLineItem;
import kitchenpos.domain.orderLineItem.OrderLineItemRepository;
import kitchenpos.domain.orderLineItem.OrderLineItems;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
        final OrderTable orderTable = orderTableRepository.findByIdAndEmptyIsFalse(orderRequest.getOrderTableId())
                .orElseThrow(NoSuchElementException::new);
        Order order = orderRequest.toOrder(orderTable);

        final OrderLineItems orderLineItems = new OrderLineItems(order.getOrderLineItems().getOrderLineItems());
        final List<Long> menuIds = orderLineItems.findMenuIds();
        orderLineItems.vaildateSize(menuRepository.countByIdIn(menuIds));

        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems.getOrderLineItems()) {
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }

        Order saveOrder = new Order(orderTable, OrderStatus.COOKING, savedOrderLineItems);
        final Order savedOrder = orderRepository.save(saveOrder);

        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        OrderTable ordertable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(NoSuchElementException::new);
        Order order = orderRequest.toOrder(ordertable);

        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);

        savedOrder.updateOrderStatus(order.getOrderStatus());

        orderRepository.save(savedOrder);

        return OrderResponse.of(savedOrder);
    }
}

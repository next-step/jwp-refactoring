package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository,
        final OrderValidator orderValidator
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order order = generateOrder(orderRequest);
        final OrderLineItems orderLineItems = new OrderLineItems(order.getOrderLineItems());
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 ID 입니다."));
        final long menuCount = menuRepository.countByIdIn(orderLineItems.toMenuIds());

        orderValidator.validateSave(order, orderTable, menuCount);
        return OrderResponse.from(orderRepository.save(order));
    }

    private Order generateOrder(OrderRequest orderRequest) {
        return Order.generate(
            orderRequest.getOrderTableId(),
            orderRequest.getOrderLineItems().stream()
                .map(this::generateOrderLineItem)
                .collect(Collectors.toList())
        );
    }

    private OrderLineItem generateOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        return OrderLineItem.generate(
            orderLineItemRequest.getMenuId(),
            orderLineItemRequest.getQuantity()
        );
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.updateOrderStatus(order.getOrderStatus());

        return savedOrder;
    }
}

package kitchenpos.order.application;

import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER;

import kitchenpos.exception.KitchenposException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableValidator orderTableValidator;
    private final MenuValidator menuValidator;

    public OrderService(final OrderRepository orderRepository,
                        final OrderTableValidator orderTableValidator,
                        final MenuValidator menuValidator) {
        this.orderRepository = orderRepository;
        this.orderTableValidator = orderTableValidator;
        this.menuValidator = menuValidator;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        orderTableValidator.checkExistsOrderTable(orderRequest.getOrderTableId());
        final Order savedOrder = orderRepository.save(new Order(orderRequest.getOrderTableId(),
                                                                OrderStatus.COOKING,
                                                                getOrderLineItems(orderRequest)));
        return OrderResponse.of(savedOrder);
    }

    private List<OrderLineItem> getOrderLineItems(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems()
                           .stream()
                           .map(this::createOrderLineItem)
                           .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        menuValidator.checkExistsMenu(orderLineItemRequest.getMenuId());
        return new OrderLineItem(orderLineItemRequest.getMenuId(),
                                 orderLineItemRequest.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                              .stream()
                              .map(OrderResponse::of)
                              .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId,
                                           final OrderStatusRequest orderStatusRequest) {
        return OrderResponse.of(findOrderById(orderId).changeOrderStatus(orderStatusRequest.getOrderStatus()));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                              .orElseThrow(() -> new KitchenposException(NOT_FOUND_ORDER));
    }
}

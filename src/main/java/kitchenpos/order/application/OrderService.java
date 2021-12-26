package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.vo.Quantity;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.validator.OrderMenuValidator;
import kitchenpos.validator.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private static final String ERROR_MESSAGE_NO_ITEMS = "주문 항목이 없습니다.";

    private final OrderRepository orderRepository;
    private final OrderMenuValidator orderMenuValidator;
    private final OrderTableValidator orderTableValidator;

    public OrderService(OrderRepository orderRepository, OrderMenuValidator orderMenuValidator,
        OrderTableValidator orderTableValidator) {
        this.orderRepository = orderRepository;
        this.orderMenuValidator = orderMenuValidator;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Long orderTableId = orderRequest.getOrderTableId();
        orderTableValidator.validateNotOrderClosedTable(orderTableId);

        List<OrderLineItemRequest> requestOrderLineItems = orderRequest.getOrderLineItems();
        orderMenuValidator.validateOrderLineItems(requestOrderLineItems);
        List<OrderLineItem> orderLineItems = createOrderLineItems(requestOrderLineItems);

        Order order = new Order(orderTableId, orderLineItems);
        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> createOrderLineItems(
        List<OrderLineItemRequest> orderLineItemRequests) {
        validateExistOrderLineItems(orderLineItemRequests);

        return orderLineItemRequests.stream()
            .map(orderLineItemRequest -> createOrderLineItem(orderLineItemRequest))
            .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        return new OrderLineItem(orderLineItemRequest.getMenuId(),
            new Quantity(orderLineItemRequest.getQuantity()));
    }

    private void validateExistOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NO_ITEMS);
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.fromList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findOrder(orderId);
        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(order);
    }

    public Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(OrderNotFoundException::new);
    }
}

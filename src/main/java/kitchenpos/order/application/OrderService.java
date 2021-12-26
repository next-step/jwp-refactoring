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
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.validator.OrderMenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private static final String ERROR_MESSAGE_NO_ITEMS = "주문 항목이 없습니다.";

    private final OrderRepository orderRepository;
    private final TableService tableService;
    private final OrderMenuValidator orderMenuValidator;

    public OrderService(OrderRepository orderRepository,
        TableService tableService, OrderMenuValidator orderMenuValidator) {
        this.orderRepository = orderRepository;
        this.tableService = tableService;
        this.orderMenuValidator = orderMenuValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderMenuValidator.validateOrderLineItems(orderRequest.getOrderLineItems());
        List<OrderLineItem> orderLineItems = createOrderLineItems(
            orderRequest.getOrderLineItems());
        final OrderTable orderTable = tableService.findOrderTable(orderRequest.getOrderTableId());

        Order order = new Order(orderTable, orderLineItems);
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

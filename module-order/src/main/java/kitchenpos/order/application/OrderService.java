package kitchenpos.order.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderCreationValidator;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemFactory;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.NotExistOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderCreationValidator orderCreationValidator;
    private final OrderRepository orderRepository;
    private final OrderLineItemFactory orderLineItemFactory;

    public OrderService(OrderCreationValidator orderCreationValidator,
                        OrderRepository orderRepository, OrderLineItemFactory orderLineItemFactory) {
        this.orderCreationValidator = orderCreationValidator;
        this.orderRepository = orderRepository;
        this.orderLineItemFactory = orderLineItemFactory;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = convertToOrder(orderRequest);
        orderCreationValidator.validateAllMenusExist(order.getOrderLineItems());
        orderCreationValidator.validateTableToMakeOrder(order.getOrderTableId());
        return OrderResponse.of(orderRepository.save(order));
    }

    private Order convertToOrder(OrderRequest orderRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();
        List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(orderLineItemDto -> orderLineItemFactory.createOrderLineItem(
                        orderLineItemDto.getMenuId(), orderLineItemDto.getQuantity())
                )
                .collect(toList());

        return new Order(orderRequest.getOrderTableId(), orderLineItems);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NotExistOrderException::new);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()));
        return OrderResponse.of(orderRepository.save(savedOrder));
    }
}

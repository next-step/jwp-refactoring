package ktichenpos.order.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import ktichenpos.order.domain.Order;
import ktichenpos.order.domain.OrderCreationValidator;
import ktichenpos.order.domain.OrderLineItem;
import ktichenpos.order.domain.OrderLineItemFactory;
import ktichenpos.order.domain.OrderRepository;
import ktichenpos.order.domain.OrderStatus;
import ktichenpos.order.dto.OrderLineItemRequest;
import ktichenpos.order.dto.OrderRequest;
import ktichenpos.order.dto.OrderResponse;
import ktichenpos.order.exception.NotExistOrderException;
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

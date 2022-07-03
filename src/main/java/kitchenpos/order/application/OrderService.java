package kitchenpos.order.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderCreationValidator;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.NotExistOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderCreationValidator orderCreationValidator;
    private final OrderRepository orderRepository;

    public OrderService(
            final OrderCreationValidator orderCreationValidator,
            final OrderRepository orderRepository
    ) {
        this.orderCreationValidator = orderCreationValidator;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = convertToOrder(orderRequest);
        orderCreationValidator.validateAllMenusExist(order.getOrderLineItems());
        orderCreationValidator.validateTableToMakeOrder(order.getOrderTableId());
        return OrderResponse.of(orderRepository.save(order));
    }

    private Order convertToOrder(OrderRequest orderRequest) {
        List<OrderLineItemDto> orderLineItemDtos = orderRequest.getOrderLineItemDtos();
        List<OrderLineItem> orderLineItems = orderLineItemDtos.stream()
                .map(OrderLineItemDto::toOrderLineItem)
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

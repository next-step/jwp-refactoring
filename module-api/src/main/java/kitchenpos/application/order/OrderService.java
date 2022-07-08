package kitchenpos.application.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.repository.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderValidator.tableValidIsEmpty(orderRequest.getOrderTableId());

        Order order = Order.from(orderTable);

        Order savedOrder = orderRepository.save(order);

        orderValidator.orderLineItemsValidation(orderRequest.getOrderLineItems());
        for (OrderLineItemRequest items :orderRequest.getOrderLineItems()) {
            savedOrder.addOrderLineItem(items.getMenuId(), items.getQuantity());
        }

        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return OrderResponse.ofResponses(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                                                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.from(savedOrder);
    }
}

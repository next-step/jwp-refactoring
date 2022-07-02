package kitchenpos.service.order.application;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.dto.OrdersRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrdersValidator ordersValidator;
    private final OrdersRepository ordersRepository;

    public OrderService(final OrdersValidator ordersValidator, final OrdersRepository ordersRepository) {
        this.ordersValidator = ordersValidator;
        this.ordersRepository = ordersRepository;
    }

    @Transactional
    public OrderResponse create(final OrdersRequest request) {
        ordersValidator.validate(request);

        final Orders orders = new Orders(request.getOrderTableId(), OrderStatus.COOKING);
        ordersRepository.save(orders);

        for (final OrderLineItemRequest orderLineItem : request.getOrderLineItems()) {
            orders.add(new OrderLineItem(orders.getId(), orderLineItem.getMenuId(), orderLineItem.getQuantity()));
        }

        return new OrderResponse(orders);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return ordersRepository.findAll().stream().map(OrderResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Orders savedOrders = ordersRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
        savedOrders.updateStatus(request.getOrderStatus());
        return new OrderResponse(savedOrders);
    }
}

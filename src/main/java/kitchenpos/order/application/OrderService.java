package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderResponses;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public OrderService(final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository,
        OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderLineItems orderLineItems = new OrderLineItems(orderRequest.getOrderLineItemsEntity());
        orderValidator.validateMenu(orderLineItems);

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(KitchenposNotFoundException::new);
        orderTable.validateNotEmpty();

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        return OrderResponse.from(orderRepository.save(order));
    }

    public OrderResponses list() {
        final List<Order> orders = orderRepository.findAll();

        return OrderResponses.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(KitchenposNotFoundException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.updateOrderStatus(orderStatus);

        orderRepository.save(savedOrder);

        return OrderResponse.from(savedOrder);
    }
}

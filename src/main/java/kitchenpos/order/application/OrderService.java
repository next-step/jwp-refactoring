package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.*;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;

    public OrderService(OrderValidator orderValidator, OrderRepository orderRepository) {
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderValidator.findOrderTable(orderRequest.getOrderTableId());
        final Order order = new Order(orderTable.getId());
        final List<OrderLineItem> orderLineItems = orderValidator.toOrderLineItems(orderRequest.getOrderLineItems(), order);
        order.addLineItems(orderLineItems);

        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.updateOrderStatus(order.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }


}

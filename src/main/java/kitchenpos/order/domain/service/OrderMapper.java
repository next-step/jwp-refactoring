package kitchenpos.order.domain.service;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public OrderMapper(OrderTableRepository orderTableRepository, OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    public Order mapToFrom(OrderRequest orderRequest) {
        return new Order(findOrderTableById(orderRequest.getOrderTableId()),
                createOrderLineItems(orderRequest.getOrderLineItems()), orderValidator);
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문테이블이 존재하지 않습니다."));
    }

    private List<OrderLineItem> createOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItemRequests.stream()
                .forEach(orderLineItemRequest -> orderLineItems.add(new OrderLineItem(orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity())));
        return orderLineItems;
    }
}

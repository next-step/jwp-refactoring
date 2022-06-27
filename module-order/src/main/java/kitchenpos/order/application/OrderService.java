package kitchenpos.order.application;

import com.google.common.collect.Lists;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.NotFoundOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
        Order order = Order.from(orderRequest.getOrderTableId());
        List<OrderLineItem> orderLineItems = this.findOrderLineItems(orderRequest.getOrderLineItems());
        order.addAllOrderLineItems(orderLineItems);

        orderValidator.validate(order);

        return OrderResponse.from(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return OrderResponse.fromList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order order = this.findOrder(orderId);

        orderValidator.validateChangeOrderStatus(order);

        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> findOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        List<OrderLineItem> result = Lists.newArrayList();
        for (OrderLineItemRequest orderLineItem : orderLineItems) {
            result.add(OrderLineItem.of(orderLineItem.getMenuId(), orderLineItem.getQuantity()));
        }
        return result;
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundOrderException(orderId));
    }

    public boolean isExistDontUnGroupState(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds,
                OrderStatus.dontUngroupStatus()
        );
    }

    public boolean isExistDontUnGroupState(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId,
                OrderStatus.dontUngroupStatus()
        );
    }
}

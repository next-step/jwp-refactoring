package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class OrderStatusService {
    public static final String COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE = "주문완료일 경우 주문상태를 변경할 수 없다.";
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderStatusService(final OrderRepository orderRepository, final OrderLineItemRepository orderLineItemRepository) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(order.getOrderStatus()).name());

//        savedOrder.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return orderRepository.save(savedOrder);
    }
}

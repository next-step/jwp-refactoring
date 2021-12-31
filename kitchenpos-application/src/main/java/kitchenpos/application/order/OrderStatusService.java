package kitchenpos.application.order;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.CanNotChangeOrderStatusException;
import kitchenpos.application.order.dto.OrderRequest;
import kitchenpos.application.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderStatusService {
    private static final String CHANGE_ORDER_STATUS_ERROR_MESSAGE = "존재하지 않는 주문은 주문 상태를 변경할 수 없습니다.";
    private final OrderRepository orderRepository;

    public OrderStatusService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    throw new CanNotChangeOrderStatusException(CHANGE_ORDER_STATUS_ERROR_MESSAGE);
                });
        savedOrder.updateOrderStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }
}

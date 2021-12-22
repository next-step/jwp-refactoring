package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.CanNotChangeOrderStatusException;
import kitchenpos.order.infra.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderStatusService {
    private final OrderRepository orderRepository;

    public OrderStatusService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    throw new CanNotChangeOrderStatusException("존재하지 않는 주문은 주문 상태를 변경할 수 없습니다.");
                });
        validateChangeOrderStatus(savedOrder);
        savedOrder.updateOrderStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    private void validateChangeOrderStatus(Order order) {
        if (order.isCompletion()) {
            throw new CanNotChangeOrderStatusException("완료된 주문은 주문상태를 변경할 수 없습니다.");
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public boolean isCookingOrMealStateByOrderTableId(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public boolean isCookingOrMealStateByOrderTableIds(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}

package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
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
                .orElseThrow(IllegalArgumentException::new);
        validateChangeOrderStatus(savedOrder);
        savedOrder.updateOrderStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    private void validateChangeOrderStatus(Order order) {
        if (order.isCompletion()) {
            throw new IllegalArgumentException();
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

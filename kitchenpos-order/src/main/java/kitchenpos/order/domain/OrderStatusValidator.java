package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableStatusValidator;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderStatusValidator implements OrderTableStatusValidator {
    private static final String ERROR_MESSAGE_INVALID_ORDER_STATUS = "주문 상태가 '조리' 또는 '식사'가 아니어야 합니다.";
    private final OrderRepository orderRepository;

    public OrderStatusValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_ORDER_STATUS);
        }
    }

    @Override
    public void validateOrderTablesStatus(List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_ORDER_STATUS);
        }
    }
}

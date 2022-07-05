package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TableValidator {
    private static final int MIN_NUMBER_OF_GUESTS = 0;
    private static final String ERROR_MESSAGE_INVALID_ORDER_STATUS = "주문 상태가 '조리' 또는 '식사'가 아니어야 합니다.";
    private static final String ERROR_MESSAGE_INVALID_NUMBER_OF_GUESTS = "방문객 수는 " + MIN_NUMBER_OF_GUESTS + " 명 이상이어야 합니다.";
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOrderTableEqualsSize(List<OrderTable> savedOrderTables, List<Long> orderTableIds) {
        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateOrderTablesStatus(List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_ORDER_STATUS);
        }
    }

    public void validateOrderStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_ORDER_STATUS);
        }
    }

    public void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_NUMBER_OF_GUESTS);
        }
    }
}

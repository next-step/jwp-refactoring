package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.message.OrderTableMessage;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class OrderTableValidator {

    private static final List<OrderStatus> INVALID_ORDER_STATUS = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        if(isCookingOrMealState(Collections.singletonList(orderTable.getId()))) {
            throw new IllegalArgumentException(OrderTableMessage.CHANGE_EMPTY_ERROR_INVALID_ORDER_STATE.message());
        }
    }

    private boolean isCookingOrMealState(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, INVALID_ORDER_STATUS);
    }

    public void validateUnGroup(OrderTables orderTable) {
        if(isCookingOrMealState(orderTable.getIds())) {
            throw new IllegalArgumentException(OrderTableMessage.UN_GROUP_ERROR_INVALID_ORDER_STATE.message());
        }
    }
}

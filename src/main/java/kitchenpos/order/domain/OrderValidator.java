package kitchenpos.order.domain;

import kitchenpos.order.message.OrderMessage;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.message.OrderTableMessage;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class OrderValidator {

    private static final List<OrderStatus> INVALID_ORDER_STATUS = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderRepository orderRepository,
                          OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(EntityNotFoundException::new);

        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException(OrderMessage.CREATE_ERROR_ORDER_TABLE_IS_EMPTY.message());
        }
    }

    public void validateChangeTableEmpty(OrderTable orderTable) {
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

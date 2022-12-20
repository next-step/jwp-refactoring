package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class TableGroupValidator {
    private static final int GROUP_TABLE_MIN_SIZE = 2;
    private static final String EXCEPTION_MESSAGE_ALREADY_COOKING_OR_MEAL = "현재 속해있는 테이블의 주문이 요리중이거나, 식사중입니다.";
    private static final String EXCEPTION_MESSAGE_MIN_GROUP_TABLE = "단체 테이블은 테이블" + GROUP_TABLE_MIN_SIZE + "개 이상 필요로 합니다.";
    private static final String EXCEPTION_MESSAGE_IS_NOT_EMPTY_TABLE = "단체로 지정할 테이블 중 비어있지 않은 테이블이 존재합니다.";
    private static final String EXCEPTION_MESSAGE_ALREADY_GROUPING = "이미 다른 단체 테이블에 속해있습니다.";

    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateEnGroup(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < GROUP_TABLE_MIN_SIZE) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_MIN_GROUP_TABLE);
        }
        if (isNotEmptyOrderTables(orderTables)) {
            throw new IllegalStateException(EXCEPTION_MESSAGE_IS_NOT_EMPTY_TABLE);
        }
        if (isAlreadyGroup(orderTables)) {
            throw new IllegalStateException(EXCEPTION_MESSAGE_ALREADY_GROUPING);
        }
    }

    public void validateUnGroup(List<Long> orderTableIds) {
        if (isExistsByOrderTableIdInAndOrderStatusIn(orderTableIds)) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_ALREADY_COOKING_OR_MEAL);
        }
    }

    private boolean isExistsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }

    private boolean isNotEmptyOrderTables(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());
    }

    private boolean isAlreadyGroup(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> null != orderTable.getTableGroupId());
    }
}

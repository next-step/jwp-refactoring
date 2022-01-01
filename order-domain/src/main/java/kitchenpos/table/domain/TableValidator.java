package kitchenpos.table.domain;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    private final List<OrderStatus> notChangeableOrderStatus = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

    private final OrderRepository orderRepository;

    public TableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateEmptyChangeable(final OrderTable table) {

        if (table.isGrouped()) {
            throw new IllegalArgumentException("단체 지정되어 있는 테이블은 빈 테이블 변경을 할 수 없습니다.");
        }

        if (checkOrderStatusInvalid(table.getId())) {
            throw new IllegalArgumentException(
                "MEAL 이나 COOKING 상태의 주문이 있으면 빈 테이블 변경을 할 수 없습니다." + notChangeableOrderStatus);
        }
    }

    public void validateNumberOfGuestsChangeable(final OrderTable table) {
        if (table.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 테이블의 방문 손님 수를 변경할 수 없습니다.");
        }
    }

    public void validateTableGroupCreatable(final OrderTables orderTables) {
        for (final OrderTable orderTable : orderTables.getOrderTables()) {
            validateTableGroupCreatable(orderTable);
        }
    }

    public void validateTableGroupCreatable(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈테이블은 단체 지정할 수 없습니다.");
        }
        if (orderTable.isGrouped()) {
            throw new IllegalArgumentException("이미 단체가 있으면 단체 지정할 수 없습니다.");
        }
    }

    public void validateUngroupPossible(final OrderTables orderTables) {
        if (checkOrderStatusInvalid(orderTables)) {
            throw new IllegalArgumentException("MEAL 이나 COOKING 상태인 주문이 있으면 단체 해제할 수 없습니다.");
        }
    }

    private boolean checkOrderStatusInvalid(OrderTables orderTables) {
        List<Long> tableIds = orderTables.getOrderTableIds();
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(tableIds, notChangeableOrderStatus);

    }
    private boolean checkOrderStatusInvalid(Long tableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(tableId, notChangeableOrderStatus);
    }

}

package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.Order;
import kitchenpos.common.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.common.exception.MisMatchedOrderTablesSizeException;
import kitchenpos.common.exception.CannotUngroupException;

@Component
public class TableGroupValidator {
    private final int ORDER_TABLE_MINIMUM_SIZE = 2;

    public void validateExistsOrdersStatusIsCookingOrMeal(List<Order> orders) {
        orders.stream()
                .filter(order -> order.equalsOrderStatus(OrderStatus.COOKING) ||
                        order.equalsOrderStatus(OrderStatus.MEAL))
                .findAny()
                .ifPresent(order -> {
                    throw new CannotUngroupException("단체지정 해지를 할 수 없는 주문상태가 존재합니다. 주문 ID : " + order.getId());
                });
    }

    public void validateOrderTablesConditionForCreatingTableGroup(List<OrderTable> orderTables, int orderTableIdsCount) {
        if (orderTables.size() < ORDER_TABLE_MINIMUM_SIZE) {
            throw new IllegalArgumentException("정산 그룹 생성은 2개 이상의 테이블만 가능합니다.");
        }
        if (orderTableIdsCount != orderTables.size()) {
            throw new MisMatchedOrderTablesSizeException("입력된 항목과 조회결과가 일치하지 않습니다.");
        }
        validateOrderTableIsEmptyOrHasTableGroups(orderTables);
    }

    public void validateOrderTableIsEmptyOrHasTableGroups(List<OrderTable> orderTables) {
        orderTables.forEach(this::validateOrderTableIsEmptyOrHasTableGroup);
    }

    public void validateOrderTableIsEmptyOrHasTableGroup(OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException("정산 그룹에 포함된 테이블을 새로운 정산그룹에 포함시킬 수 없습니다.");
        }
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있지 않은 테이블은 정산 그룹에 포함시킬 수 없습니다.");
        }
    }
}

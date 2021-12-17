package kitchenpos.ordertable.domain;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderTables {

    public static final String MESSAGE_VALIDATE_MIN_COUNT = "테이블이 2개 이상이어야 합니다.";
    public static final String MESSAGE_VALIDATE_GROUPABLE = "테이블이 그룹에 등록 가능한 상태여야 합니다.";
    private static final int MIN_ORDER_TABLE_COUNT = 2;

    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void group(Long tableGroupId) {
        orderTables.forEach(this::validateGroupable);
        orderTables.forEach(it -> it.group(tableGroupId));
    }

    public void ungroup(OrderTableValidator orderTableValidator) {
        orderTables.forEach(orderTableValidator::validateOrderTableChangable);
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validate(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_COUNT) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_MIN_COUNT);
        }
    }

    private void validateGroupable(OrderTable orderTable) {
        if (!orderTable.isGroupable()) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_GROUPABLE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTables that = (OrderTables) o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }
}

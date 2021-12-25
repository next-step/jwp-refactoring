package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderTables {
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables, int expectedSize) {
        if (expectedSize != orderTables.size()) {
            throw new IllegalArgumentException("올바른 주문 테이블을 입력하세요");
        }
        return new OrderTables(orderTables);
    }

    public void group(Long tableGroupId) {
        setTableGroupToOrderTables(tableGroupId, orderTables);
    }

    private static void setTableGroupToOrderTables(Long tableGroupId, List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(tableGroupId);
            orderTable.changeToNotEmpty();
        }
    }

    public void ungroup(OrderTableValidatable orderTableValidator) {
        for (final OrderTable orderTable : orderTables) {
            orderTableValidator.validateHasProgressOrder(orderTable);
            orderTable.unsetTableGroup();
        }
        orderTables = new ArrayList<>();
    }

    public List<OrderTable> values() {
        return Collections.unmodifiableList(orderTables);
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(orderTables);
    }
}

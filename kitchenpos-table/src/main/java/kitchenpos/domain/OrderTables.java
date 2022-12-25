package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderTables {

    private static final String INVALID_LIST_SIZE_EXCEPTION = "주문 테이블 목록의 건수가 유효하지 않습니다.";
    private static final String INVALID_ORDER_TABLE_EXCEPTION = "그룹으로 지정할 테이블이 유효하지 않습니다.";

    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {

    }

    public OrderTables(List<OrderTable> orderTableList) {
        this.orderTables = orderTableList;
    }

    public void groupTables(TableGroup tableGroup) {
        validateGroupingTables();
        orderTables.stream()
                .forEach(orderTable -> {
                    orderTable.group(tableGroup);
                });
    }

    private void validateGroupingTables() {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException(INVALID_LIST_SIZE_EXCEPTION);
        }

        orderTables.stream().forEach(this::validateOrderTable);
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException(INVALID_ORDER_TABLE_EXCEPTION);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

}

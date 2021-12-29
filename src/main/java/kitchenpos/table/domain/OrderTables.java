package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {
    List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(int orderTablesSize, List<OrderTable> orderTables) {
        if (orderTablesSize != orderTables.size()) {
            throw new IllegalArgumentException("주문 테이블 정보가 존재하지 않습니다.");
        }

        if (CollectionUtils.isEmpty(orderTables) || orderTablesSize < 2) {
            throw new IllegalArgumentException("단체 지정을 하려면 주문 테이블을 2개 이상 선택해주세요.");
        }

        return new OrderTables(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void group(Long tableGroupId) {
        for (OrderTable orderTable : orderTables) {
            orderTable.group(tableGroupId);
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
        orderTables = new ArrayList<>();
    }
}

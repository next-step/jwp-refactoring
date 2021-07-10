package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    public static final String NOT_MATCH_ORDER_TABLE_SIZE = "조회 된 주문 테이블의 수와 다릅니다.";

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> orderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public void validateOrderTableSize(int size) {
        if (size != orderTables.size()) {
            throw new IllegalArgumentException(NOT_MATCH_ORDER_TABLE_SIZE);
        }
    }

    public void validateOrderTables() {
        orderTables.forEach(OrderTable::validateOrderTable);
    }

    public void mappingTableGroup(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.mappingTableGroup(tableGroup);
        }
    }

    public void unGroup() {
        orderTables.forEach(OrderTable::unGroup);
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                .map(OrderTable::id)
                .collect(Collectors.toList());
    }
}

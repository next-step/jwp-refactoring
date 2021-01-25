package kitchenpos.tablegroup.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup extends BaseEntity {
    @OneToMany(mappedBy = "tableGroup")
    private final List<OrderTable> orderTables = new ArrayList<>();

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        validate(orderTables);
        TableGroup tableGroup = new TableGroup();
        tableGroup.orderTables.addAll(orderTables);
        return tableGroup;
    }

    protected TableGroup() {
    }

    public void clearTables() {
        orderTables.forEach(OrderTable::releaseInGroup);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    private static void validate(List<OrderTable> orderTables) {
        if (isNotValidOrderTables(orderTables)) {
            throw new IllegalArgumentException("주문 그룹으로 설정될 주문 테이블은 Group으로 설정되지 않거나, 점유되어있지 않아야합니다.");
        }
    }

    private static boolean isNotValidOrderTables(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> orderTable.isNotEmpty() || orderTable.hasTableGroup());
    }
}

package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables, Long tableGroupId) {
        validTableEmpty(orderTables);
        changeGroup(orderTables, tableGroupId);
        this.orderTables = orderTables;
    }

    private void validTableEmpty(List<OrderTable> orderTables) {
        orderTables.stream()
                .filter(it -> !it.isEmpty() || it.isGroup())
                .findFirst()
                .ifPresent(e -> {
                    throw new IllegalArgumentException("테이블이 비어있거나 미그룹 상태여야 합니다.");
                });
    }

    public void changeGroup(List<OrderTable> orderTables, Long tableGroupId) {
        orderTables.forEach(orderTable -> orderTable.changeGroup(tableGroupId));
    }

    public void changeUnGroup() {
        orderTables.forEach(OrderTable::changeUnGroup);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}

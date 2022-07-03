package kitchenpos.table.domain;

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

    public OrderTables(List<OrderTable> orderTables, TableGroup tableGroup) {
        validTableEmpty(orderTables);
        changeGroup(orderTables, tableGroup);
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

    public void changeGroup(List<OrderTable> orderTables, TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.changeGroup(tableGroup));
    }

    public void changeUnGroup() {
        orderTables.forEach(orderTable -> orderTable.changeUnGroup());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}

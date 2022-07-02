package kitchenpos.table_group.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.table.domain.OrderTable;

@Embeddable
public class OrderTables {

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void addAll(TableGroup tableGroup, List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
        orderTables.forEach(orderTable -> orderTable.group(tableGroup.getId()));
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::ungroup);
        this.orderTables.clear();
    }

    public List<Long> getIds() {
        return this.orderTables.stream()
            .map(OrderTable::getTableGroupId)
            .collect(Collectors.toList());
    }

}

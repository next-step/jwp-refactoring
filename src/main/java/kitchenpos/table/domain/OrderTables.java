package kitchenpos.table.domain;

import java.util.Collections;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.tablegroup.domain.TableGroup;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroupId")
    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public boolean avaliableTable() {
        return !orderTables.stream()
            .anyMatch(table -> !table.isEmpty() || table.hasTableGroup());
    }

    public void chargedBy(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.chargedBy(tableGroup);
        }
    }

}

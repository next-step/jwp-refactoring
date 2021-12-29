package kitchenpos.table.domain;

import java.util.*;

import javax.persistence.*;

@Embeddable
public class OrderTables {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void add(Long tableGroupId, OrderTable orderTable) {
        orderTable.changeTableGroup(tableGroupId);
        orderTables.add(orderTable);
    }

    public void ungroup() {
        orderTables.forEach(it -> {
            it.initTableGroup();
        });
    }
}

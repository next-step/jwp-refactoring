package table.domain;

import java.util.*;

import javax.persistence.*;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public void add(TableGroup tableGroup, OrderTable orderTable) {
        orderTable.changeTableGroup(tableGroup);
        orderTables.add(orderTable);
    }

    public void ungroup() {
        orderTables.forEach(it -> {
            it.initTableGroup();
        });
    }
}

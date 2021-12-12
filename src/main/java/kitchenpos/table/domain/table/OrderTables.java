package kitchenpos.table.domain.table;

import kitchenpos.table.domain.tablegroup.TableGroup;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public void addOrderTable(TableGroup tableGroup, OrderTable orderTable) {
        orderTable.changeTableGroup(tableGroup);
        orderTables.add(orderTable);
    }

    public void ungroup() {
        orderTables.forEach(it -> {
            it.changeEmptyTableGroup();
        });
    }
}

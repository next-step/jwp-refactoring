package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void addOrderTable(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public void ungroupingTableGroup(){
        for (OrderTable orderTable : orderTables){
            orderTable.ungroupingTableGroup();
        }
    }

    public void groupingTableGroup(OrderTables emptyTables, TableGroup tableGroup) {
        for (OrderTable emptyTable : emptyTables.orderTables) {
            emptyTable.assignTableGroup(tableGroup);
            orderTables.add(emptyTable);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public int size() {
        return orderTables.size();
    }
}

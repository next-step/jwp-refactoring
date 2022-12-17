package kitchenpos.table.domain;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderTable> orderTables;

    protected OrderTables() {}

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = new ArrayList<>(orderTables);
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> readOnlyValue() {
        return Collections.unmodifiableList(this.orderTables);
    }

    public void setTableGroup(TableGroup tableGroup) {
        orderTables.stream().forEach(orderTable -> orderTable.setTableGroup(tableGroup));
    }

    public void unTableGroup() {
        orderTables.stream().forEach(orderTable -> orderTable.unTableGroup());
    }

    public void changeIsNotEmpty() {
        orderTables.stream().forEach(orderTable -> orderTable.setEmpty(false));
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}

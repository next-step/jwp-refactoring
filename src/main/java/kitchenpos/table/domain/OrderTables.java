package kitchenpos.table.domain;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "table_group_id")
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

    public void grouping(Long tableGroupId) {
        orderTables.stream().forEach(orderTable -> {
            orderTable.setTableGroupId(tableGroupId);
            orderTable.setEmpty(false);
        });
    }

    public void ungroup() {
        orderTables.stream().forEach(orderTable -> orderTable.ungroup());
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}

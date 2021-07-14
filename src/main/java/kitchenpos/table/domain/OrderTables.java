package kitchenpos.table.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
        this.orderTables = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void registerTableGroup(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.registerTableGroup(tableGroup));
    }

    public void unRegisterTableGroup() {
        orderTables.forEach(orderTable -> orderTable.registerTableGroup(null));
    }

    public List<Long> getOrderIds() {
        return orderTables.stream()
                .map(orderTable -> orderTable.getOrderId())
                .collect(Collectors.toList());
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(orderTable -> orderTable.getId())
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}

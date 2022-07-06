package kitchenpos.domain.table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {

    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTablesIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void allocateAll(TableGroup tableGroup) {
        orderTables.forEach(it -> it.allocateTableGroup(tableGroup));
    }

    public void deallocateAll() {
        orderTables.forEach(OrderTable::deallocateTableGroup);
        this.orderTables = new ArrayList<>();
    }
}

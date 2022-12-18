package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private final List<OrderTable> orderTableItems;

    protected OrderTables() {
        orderTableItems = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTableItems) {
        this.orderTableItems = new ArrayList<>(orderTableItems);
    }

    public void group(TableGroup tableGroup) {
        this.orderTableItems.forEach(orderTable -> orderTable.group(tableGroup));
    }

    public void unGroup() {
        this.orderTableItems.forEach(OrderTable::unGroup);
    }

    public List<OrderTable> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(this.orderTableItems));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderTables that = (OrderTables) o;

        return orderTableItems.equals(that.orderTableItems);
    }

    @Override
    public int hashCode() {
        return orderTableItems.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(orderTableItems);
    }
}

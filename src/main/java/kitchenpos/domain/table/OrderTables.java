package kitchenpos.domain.table;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(OrderTable ...orderTables) {
        this(Arrays.asList(orderTables));
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void bookedBy(TableGroup tableGroup) {
        if (isBookedAny()) {
            throw new IllegalStateException();
        }

        for (OrderTable orderTable : orderTables) {
            orderTable.bookedBy(tableGroup.getId());
        }
    }

    public void ungroup() {
        if (!isUnGroupable()) {
            throw new IllegalStateException();
        }

        orderTables.forEach(OrderTable::ungroup);
    }

    public boolean isUnGroupable() {
        boolean isUnGroupable = orderTables.stream()
                .allMatch(OrderTable::isUnGroupable);

        return isUnGroupable;
    }

    public boolean isBookedAny() {
        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.isBooked()) {
                return true;
            }
        }

        return false;
    }

    public int size() {
        return orderTables.size();
    }

    public List<OrderTable> toCollection() {
        return Collections.unmodifiableList(orderTables);
    }
}

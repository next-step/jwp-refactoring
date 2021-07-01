package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void bookedBy(TableGroup tableGroup) {
        if (isBookedAny()) {
            throw new IllegalStateException();
        }

        for (OrderTable orderTable : orderTables) {
            orderTable.bookedBy(tableGroup);
        }
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
}

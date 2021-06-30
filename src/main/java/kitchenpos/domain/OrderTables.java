package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    }

    public boolean isBookedAny() {
        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
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

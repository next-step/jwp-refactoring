package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.exception.KitchenposErrorCode;
import kitchenpos.exception.KitchenposException;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void checkSameSize(int size) {
        if (size != orderTables.size()) {
            throw new KitchenposException(KitchenposErrorCode.INVALID_TABLE_SIZE);
        }
    }

    public void checkNotContainsUsedTable() {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new KitchenposException(KitchenposErrorCode.CONTAINS_USED_TABLE);
            }
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void add(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public void unGroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.referenceTableGroup(null);
        }
    }
}

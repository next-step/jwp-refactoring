package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kitchenpos.common.Messages.TABLE_GROUP_ORDER_IDS_FIND_IN_NO_SUCH;
import static kitchenpos.common.Messages.TABLE_GROUP_ORDER_NOT_EMPTY;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables empty() {
        return new OrderTables();
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void validateOrderTableGroup(List<Long> requestOrderTablesIds) {
        if (requestOrderTablesIds.size() != orderTables.size()) {
            throw new IllegalArgumentException(TABLE_GROUP_ORDER_IDS_FIND_IN_NO_SUCH);
        }

        for (final OrderTable orderTable : orderTables) {
            if (orderTable.isNotEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException(TABLE_GROUP_ORDER_NOT_EMPTY);
            }
        }
    }

    public void add(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public void ungroup() {
        orderTables.forEach(orderTable -> orderTable.setTableGroup(null));
    }
}

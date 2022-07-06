package kitchenpos.tablegroup.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.ordertable.domain.OrderTable;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables= new ArrayList<>();
    public static final int MIN_ORDER_TABLE_NUMBER = 2;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    public void assignOrderTables(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.groupBy(tableGroup));
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesDuplicated(orderTables);
        validateOrderTablesNotGrouped(orderTables);
        validateOrderTablesEmpty(orderTables);
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (orderTables == null || orderTables.size() < MIN_ORDER_TABLE_NUMBER) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    private void validateOrderTablesDuplicated(List<OrderTable> orderTables) {
        if(orderTables.size() != orderTables.stream().distinct().count()){
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesNotGrouped(List<OrderTable> orderTables) {
        if (orderTables.stream().
            anyMatch(orderTable -> orderTable.isGrouped())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream().
            anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalArgumentException();
        }
    }


    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

}

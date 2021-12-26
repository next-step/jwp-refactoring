package kitchenpos.ordertable.domain;

import kitchenpos.common.exception.MinimumOrderTableNumberException;
import org.springframework.util.CollectionUtils;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        validateTableGroup();
    }

    private void validateTableGroup() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new MinimumOrderTableNumberException();
        }

        validateOrderTables();
    }

    private void validateOrderTables() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.validateAddableOrderTable();
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void ungroup() {
        for (OrderTable orderTable: orderTables) {
            orderTable.ungroupTableGroup();
        }
    }
}

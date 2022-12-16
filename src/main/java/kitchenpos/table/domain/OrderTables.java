package kitchenpos.table.domain;

import kitchenpos.exception.OrderTableError;
import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    private static final int MIN_ORDER_TABLES_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {

    }

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public void group(Long tableGroupId) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException(OrderTableError.REQUIRED_ORDER_TABLE_LIST);
        }
        if (orderTables.size() < MIN_ORDER_TABLES_SIZE) {
            throw new IllegalArgumentException(OrderTableError.INVALID_ORDER_TABLE_LIST_SIZE);
        }
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(tableGroupId));
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public int size() {
        return orderTables.size();
    }
}

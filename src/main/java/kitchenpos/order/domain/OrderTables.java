package kitchenpos.order.domain;

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

    public OrderTables() {

    }

    public void group(TableGroup tableGroup, List<OrderTable> target) {
        validate(target);
        target.forEach(orderTable -> {
            orderTable.checkOrderTableIsEmpty();
            orderTable.changeEmpty(false, Collections.emptyList());
            addOrderTable(tableGroup, orderTable);
        });
    }

    private void validate(List<OrderTable> target) {
        if (CollectionUtils.isEmpty(target)) {
            throw new IllegalArgumentException();
        }

        if (target.size() < MIN_ORDER_TABLES_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    public void addOrderTable(TableGroup tableGroup, OrderTable orderTable) {
        if (!hasOrderTable(orderTable)) {
            this.orderTables.add(orderTable);
            orderTable.updateTableGroup(tableGroup);
        }
    }

    private boolean hasOrderTable(OrderTable orderTable) {
        return this.orderTables.contains(orderTable);
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

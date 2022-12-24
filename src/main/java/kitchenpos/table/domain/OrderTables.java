package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "table_group_id")
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

        TableGroupCreateValidator.validateOrderTableMinSize(target);
    }

    public void addOrderTable(TableGroup tableGroup, OrderTable orderTable) {
        if (!hasOrderTable(orderTable)) {
            this.orderTables.add(orderTable);
            orderTable.updateTableGroupId(tableGroup.getId());
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

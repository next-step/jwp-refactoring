package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> values;

    protected OrderTables() {
        this(new ArrayList<>());
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.values = copy(orderTables);
    }

    private static List<OrderTable> copy(List<OrderTable> orderTables) {
        return orderTables.stream().map(OrderTable::from).collect(Collectors.toList());
    }

    public static OrderTables create() {
        return new OrderTables();
    }

    public List<OrderTable> value() {
        return Collections.unmodifiableList(values);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public boolean hasBiggerOrEqualSizeThan(int size) {
        return values.size() >= size;
    }

    public boolean hasEmptyOrderTable() {
        return values.stream()
                .anyMatch(OrderTable::isEmpty);
    }

    public boolean hasGroupedTable() {
        return values.stream()
                .map(OrderTable::getTableGroupId)
                .anyMatch(Objects::nonNull);
    }

    public void group(TableGroup tableGroup) {
        values.forEach(orderTable -> orderTable.setGroup(tableGroup.getId()));
    }

    public void ungroup() {
        values.forEach(OrderTable::resetGroup);
        values.clear();
    }
}

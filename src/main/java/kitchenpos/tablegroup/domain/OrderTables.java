package kitchenpos.tablegroup.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.table.domain.OrderTable;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    public static OrderTables of(final List<OrderTable> orderTableList) {
        OrderTables orderTables = new OrderTables();
        orderTables.addAll(orderTableList);
        return orderTables;
    }

    public void add(OrderTable orderTable) {
        if (this.orderTables.contains(orderTable)) {
            return;
        }
        this.orderTables.add(orderTable);
    }

    public void addAll(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }


    public <R> List<R> convertAll(Function<OrderTable, R> converter) {
        return this.orderTables.stream()
                               .map(converter)
                               .collect(Collectors.toList());
    }

    public void checkEmptyAndNotIncludeTableGroup() {
        this.orderTables.forEach(this::checkEmptyAndNotIncludeOrderTable);
    }

    private void checkEmptyAndNotIncludeOrderTable(final OrderTable orderTable) {
        orderTable.checkEmpty();
        orderTable.checkNotIncludeTableGroup();
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.stream()
                               .map(OrderTable::getId)
                               .collect(Collectors.toList());
    }

    public void grouping(final TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.grouping(tableGroup));
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::ungroup);
    }
}

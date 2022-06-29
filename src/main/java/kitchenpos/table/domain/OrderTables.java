package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = ALL)
    private final List<OrderTable> orderTables;

    public OrderTables() {
        this(new ArrayList<>());
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> values() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void addTableGroupAndEmpties(boolean empty, TableGroup tableGroup) {
        orderTables.forEach(savedOrderTable -> {
            savedOrderTable.changeTableGroupIdAndEmpty(tableGroup);
            savedOrderTable.changeEmpty(empty);
        });
    }

    public void validateEmptyAndTableGroups() {
        orderTables.forEach(OrderTable::validateEmptyAndTableGroup);
    }
}

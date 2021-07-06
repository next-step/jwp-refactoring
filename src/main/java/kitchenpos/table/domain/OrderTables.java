package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> orderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public void validateDbDataSize(int requestSize) {
        if (requestSize != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateOrderTables() {
        orderTables.forEach(OrderTable::validateOrderTable);
    }

    public void createTableGroup(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.mappingTableGroup(tableGroup);
        }
    }

    public void unGroup() {
        orderTables.forEach(OrderTable::unGroup);
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                .map(OrderTable::id)
                .collect(Collectors.toList());
    }
}

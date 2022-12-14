package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "id", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = new ArrayList<>(orderTables);
    }

    public static OrderTables toBeGrouped(List<OrderTable> savedOrderTables) {
        long countOfOrderTableCanBeAddedToTableGroup = savedOrderTables.stream()
                .filter(OrderTable::canBeAddedToTableGroup)
                .count();

        if (savedOrderTables.size() != countOfOrderTableCanBeAddedToTableGroup) {
            throw new IllegalArgumentException();
        }


        return new OrderTables(savedOrderTables);
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            return new OrderTables(new ArrayList<>());
        }
        return new OrderTables(orderTables);
    }

    public List<OrderTable> getTables() {
        return Collections.unmodifiableList(this.orderTables);
    }

    public void addTable(OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }

    public List<Long> getTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public int size() {
        return this.orderTables.size();
    }

    public void unGroup() {
        this.orderTables.forEach(orderTable -> orderTable.assignTableGroup(null));
        this.orderTables.clear();
    }

    public void group(TableGroup tableGroup) {
        this.orderTables.forEach(savedOrderTable -> {
            savedOrderTable.setEmpty(false);
            savedOrderTable.assignTableGroup(tableGroup);
        });
    }
}

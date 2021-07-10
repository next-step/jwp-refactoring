package kitchenpos.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    protected OrderTables() {
    }

    public List<Long> ids() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    public List<Long> tableGroupIds() {
        return orderTables.stream()
            .map(OrderTable::getTableGroupId)
            .collect(Collectors.toList());
    }

    public void ungroup(Orders orders) {
        validateOrderStatus(orders);

        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateOrderStatus(Orders orders) {
        orderTables.stream()
            .filter(it -> orders.isNotCompleted(it.getId()))
            .findFirst()
            .ifPresent(orderTable -> {
                throw new IllegalArgumentException();
            });
    }

    public List<OrderTable> toList() {
        return Collections.unmodifiableList(orderTables);
    }

    public int size() {
        return orderTables.size();
    }

    public void changeTableGroupId(Long tableGroupId) {
        orderTables.forEach(orderTable -> {
            orderTable.changeTableGroupId(tableGroupId);
            orderTable.occupy();
        });
    }

    public boolean hasOccupiedTable() {
        return orderTables.stream()
            .anyMatch(OrderTable::isOccupied);
    }

    public boolean hasTableGroupId() {
        return orderTables.stream()
            .anyMatch(orderTable -> orderTable.getId() != null);
    }

    public void addAll(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }
}

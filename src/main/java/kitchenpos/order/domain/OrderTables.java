package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables(final List<OrderTable> orderTables) {
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

    public void ungroup(final Orders orders) {
        validateOrderStatus(orders);

        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateOrderStatus(final Orders orders) {
        orderTables.stream()
            .filter(it -> orders.isNotCompleted(it.getId()))
            .findFirst()
            .ifPresent(orderTable -> {
                throw new IllegalArgumentException("사용 중인 테이블은 그룹을 해제할 수 없습니다.");
            });
    }

    public List<OrderTable> toList() {
        return Collections.unmodifiableList(orderTables);
    }

    public int size() {
        return orderTables.size();
    }

    public void changeTableGroupId(final Long tableGroupId) {
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
            .anyMatch(orderTable -> orderTable.getTableGroupId() != null);
    }

    public void addAll(final List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public void updateTableGroup(final TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(tableGroup));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final OrderTables that = (OrderTables)o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }
}

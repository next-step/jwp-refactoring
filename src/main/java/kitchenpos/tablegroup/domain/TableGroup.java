package kitchenpos.tablegroup.domain;

import kitchenpos.BaseEntity;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() { }

    public TableGroup(Long id) {
        this.id = id;
    }

    public TableGroup(List<OrderTable> orderTables) {
        validate(orderTables);

        this.orderTables = orderTables;
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        validate(orderTables);

        this.id = id;
        this.orderTables = orderTables;
    }

    public static TableGroup of(List<OrderTable> savedOrderTables) {
        return new TableGroup(savedOrderTables);
    }

    private void validate(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id) && Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTables);
    }

    public void grouping() {
        validateGroupingSizeWith(orderTables);
        validateOrderTablesForGrouping(orderTables);
        orderTablesSetting(orderTables);
    }

    private void validateGroupingSizeWith(List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("그룹지으려는 주문테이블 개수가 올바르지 않음.");
        }
    }

    private void validateOrderTablesForGrouping(List<OrderTable> orderTables) {
        orderTables.stream()
                .forEach(OrderTable::validateForGrouping);
    }

    private void orderTablesSetting(List<OrderTable> orderTables) {
        orderTables.stream()
                .forEach(orderTable -> orderTable.groupingIn(this.id));
    }

    public void ungrouping() {
        orderTables.stream()
                .forEach(OrderTable::ungrouping);
    }

}

package kitchenpos.domain;

import kitchenpos.exception.IllegalOrderTableException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup {
    @Transient
    public static final int MIN__NUMBER_TABLES = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private final LocalDateTime createdDate = LocalDateTime.now();

    @OneToMany(mappedBy = "tableGroup")
    private final List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        validate(orderTables);
        addOrderTables(orderTables);
    }

    private void addOrderTables(List<OrderTable> orderTables) {
        orderTables.forEach(
            it -> this.orderTables.add(it.groupBy(this))
        );
    }

    private void validate(List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < MIN__NUMBER_TABLES) {
            throw new IllegalOrderTableException();
        }

        if (!targetTablesEmpty(orderTables)) {
            throw new IllegalOrderTableException();
        }
    }

    private boolean targetTablesEmpty(List<OrderTable> orderTables) {
        return orderTables.stream().allMatch(OrderTable::isEmpty);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }
}

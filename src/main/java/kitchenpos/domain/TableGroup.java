package kitchenpos.domain;

import kitchenpos.exception.IllegalOrderTableException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalOrderTableException();
        }

        if (!targetTablesEmpty(orderTables)) {
            throw new IllegalOrderTableException();
        }

        if(!targetTablesOrderFinished(orderTables)) {
            throw new IllegalOrderTableException();
        }
    }

    private boolean targetTablesEmpty(List<OrderTable> orderTables) {
        return orderTables.stream().allMatch(OrderTable::isEmpty);
    }

    private boolean targetTablesOrderFinished(List<OrderTable> orderTables) {
        return orderTables.stream().allMatch(OrderTable::isOrderFinished);
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
}

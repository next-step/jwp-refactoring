package kitchenpos.domain;

import kitchenpos.exception.IllegalOrderTablesException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    public static final int ORDERTABLE_MIN = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
        validateOrderTables();
        groupOrderTables(orderTables);
    }

    private void groupOrderTables(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> orderTable.changeTableGroup(this));
        orderTables.forEach(orderTable -> orderTable.changeEmpty(false));
    }

    private void validateOrderTables() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < ORDERTABLE_MIN) { //
            throw new IllegalOrderTablesException();
        }
        for (final OrderTable savedOrderTable : orderTables) {
            validateOrderTable(savedOrderTable);
        }
    }

    private void validateOrderTable(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalOrderTablesException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::ungroup);
    }
}

package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public TableGroup() {}

    private TableGroup(List<OrderTable> tables) {
        for (final OrderTable savedOrderTable : tables) {
            isValidOrderTable(savedOrderTable);
        }
        this.orderTables = tables;
    }

    public TableGroup(Long id) {
        this.id = id;
    }

    public static TableGroup of(List<OrderTable> tables) {
        return new TableGroup(tables);
    }

    public static TableGroup of(Long id) {
        return new TableGroup(id);
    }

    public static TableGroup create(List<OrderTable> savedOrderTables) {
        TableGroup tableGroup = new TableGroup(savedOrderTables);
        savedOrderTables.forEach(savedOrderTable -> {
            savedOrderTable.assignTableGroup(tableGroup);
        });
        return tableGroup;
    }

    private void isValidOrderTable(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
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

    public void createId(Long id) {
        this.id = id;
    }
}

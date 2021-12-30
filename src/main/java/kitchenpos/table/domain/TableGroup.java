package kitchenpos.table.domain;

import kitchenpos.table.exception.NotCreateTableGroupException;
import kitchenpos.table.exception.TableErrorCode;
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
            throw new NotCreateTableGroupException(TableErrorCode.ALREADY_ASSIGN_GROUP);
        }
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

    public void createId(Long id) {
        this.id = id;
    }
}

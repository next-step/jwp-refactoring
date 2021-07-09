package kitchenpos.tablegroup.domain;

import static java.util.Arrays.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroupId;

@Entity
public class TableGroup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private LocalDateTime createdDate;

    protected TableGroup() {}

    TableGroup(Long id, LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public TableGroup(LocalDateTime createdDate) {
        this(null, createdDate);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void ungroup(List<OrderTable> tables, UngroupValidator ungroupValidator) {
        OrderTables orderTables = OrderTables.of(tables);
        orderTables.ungrouped(ungroupValidator);
    }

    public void group(List<OrderTable> tables) {
        OrderTables orderTables = OrderTables.of(tables);
        orderTables.grouped(new TableGroupId(getId()));
    }

    void group(OrderTable... tables) {
        group(asList(tables));
    }
}

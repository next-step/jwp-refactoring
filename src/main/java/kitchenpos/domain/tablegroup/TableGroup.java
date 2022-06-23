package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.domain.table.OrderTable;

@Entity
@Table(name = "table_group")
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    protected TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup create() {
        return new TableGroup();
    }

    public static TableGroup from(Long id) {
        return new TableGroup(id);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void assignedOrderTables(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> orderTable.mappedByTableGroup(this.getId()));
    }
}

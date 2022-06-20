package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
    @Embedded
    private OrderTables orderTables = OrderTables.createEmpty();

    protected TableGroup() {}

    private TableGroup(List<OrderTable> orderTables) {
        this.orderTables = OrderTables.from(orderTables);
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = OrderTables.from(orderTables);
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    public static TableGroup of(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public void assignedOrderTables(List<OrderTable> orderTables) {
        this.orderTables = OrderTables.from(orderTables);
        orderTables.forEach(orderTable -> orderTable.mappedByTableGroup(this));
    }
}

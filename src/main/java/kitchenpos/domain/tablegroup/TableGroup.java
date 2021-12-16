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
import kitchenpos.domain.table.OrderTables;

@Entity
@Table(name = "table_group")
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = OrderTables.createEmpty();

    protected TableGroup() {}

    private TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    private TableGroup(List<OrderTable> orderTables) {
        this.orderTables = OrderTables.from(orderTables);
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup from(Long id) {
        return new TableGroup(id);
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
        orderTables.forEach(orderTable -> orderTable.alignTableGroup(this));
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
}

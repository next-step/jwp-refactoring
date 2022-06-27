package kitchenpos.tableGroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.table.domain.OrderTable;

@Entity
@Table(name = "table_group")
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    private TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup generate() {
        return new TableGroup();
    }

    public static TableGroup of(Long id) {
        return new TableGroup(id);
    }

    public void mapIntoTable(List<OrderTable> items) {
        orderTables.addAll(id, items);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getItems();
    }

    public void unGroup() {
        this.orderTables.unGroup();
    }
}

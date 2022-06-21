package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "table_group")
public class TableGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroupEntity() {
    }

    private TableGroupEntity(List<OrderTableEntity> orderTables) {
        this.createdDate = LocalDateTime.now();
        this.orderTables.addAll(orderTables);
    }

    public static TableGroupEntity from(List<OrderTableEntity> orderTables) {
        return new TableGroupEntity(orderTables);
    }

    public void validateTablesEmpty() {
        this.orderTables.validateTablesEmpty();
    }

    public void tablesMapIntoGroup() {
        this.orderTables.tablesMapIntoGroup(id);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableEntity> getOrderTables() {
        return orderTables.getItems();
    }

    public void unGroup() {
        this.orderTables.unGroup();
    }
}

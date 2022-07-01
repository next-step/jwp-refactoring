package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "table_group")
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = OrderTables.empty();

    protected TableGroup() {
    }

    private TableGroup(OrderTables orderTables) {
        this.createdDate = LocalDateTime.now();
        addOrderTable(orderTables);
    }

    private TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(OrderTables orderTables) {
        return new TableGroup(orderTables);
    }

    public static TableGroup of(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    private void addOrderTable(OrderTables orderTables) {
        orderTables.getOrderTables().forEach(orderTable -> {
            orderTable.setTableGroup(this);
            orderTable.setEmpty(Empty.of(false));
            this.orderTables.add(orderTable);
        });
    }
}

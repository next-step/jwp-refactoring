package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
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

    private TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    private TableGroup(Long id, LocalDateTime createdDate) {
        this(createdDate);
        this.id = id;
    }

    private TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this(id, createdDate);
        this.orderTables = orderTables;
    }

    public static TableGroup of(LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }

    public static TableGroup of(Long id, LocalDateTime createdDate) {
        return new TableGroup(id, createdDate);
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

    public void addOrderTable(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable ->
        {
            orderTable.setTableGroup(this);
            orderTable.setEmpty(Empty.of(false));
            this.orderTables.add(orderTable);
        });
    }
}

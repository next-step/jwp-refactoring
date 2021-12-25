package kitchenpos.table.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    private TableGroup(Long id, OrderTables orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public static TableGroup of(long id) {
        return new TableGroup(id, new OrderTables());
    }

    public static TableGroup of(Long id, OrderTables orderTables) {
        return new TableGroup(id, orderTables);
    }

    public static TableGroup of() {
        return new TableGroup(null, new OrderTables());
    }

    public void addOrderTable(OrderTable orderTable) {
        orderTable.grouping(this);
        orderTables.add(orderTable);
    }

    public void ungroup() {
        orderTables.ungroup();
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
}

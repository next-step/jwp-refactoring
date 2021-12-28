package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup extends AbstractAggregateRoot<TableGroup> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
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

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTableIds();
    }

    public void groupTables(final List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> this.orderTables.addOrderTable(this, orderTable));
    }

    public void ungroup() {
        registerEvent(new TableUngroupedEvent(this));

        orderTables.ungroup();
    }
}


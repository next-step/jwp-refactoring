package kitchenpos.tableGroup.domain;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
        this.orderTables = new OrderTables();
    }

    public TableGroup(List<OrderTable> orderTables) {
        this.orderTables = new OrderTables();
        addOrderTables(orderTables);
    }

    private void addOrderTables(final List<OrderTable> orderTables) {
        orderTables.stream()
                .forEach(orderTable -> {
                    orderTable.changeEmpty(false);
                    orderTable.updateTableGroup(this);
                    this.orderTables.add(orderTable);
                });
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

    public void ungroup() {
        this.orderTables.ungroup();
    }
}

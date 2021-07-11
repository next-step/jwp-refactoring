package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.OrderTables;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "table_group")
@EntityListeners(AuditingEntityListener.class)
public class TableGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    public TableGroupEntity() {
    }

    public TableGroupEntity(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroupEntity(OrderTables orderTables) {
        updateOrderTables(orderTables);
    }

    public TableGroupEntity(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void updateOrderTables(OrderTables orderTables) {
        orderTables.updateTableGroup(this);
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableEntity> getOrderTables() {
        return orderTables.values();
    }

    public void unGroup() {
        orderTables.releaseGroup();
    }
}

package kitchenpos.table.domain;

import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup extends AbstractAggregateRoot<TableGroup> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public void addOrderTables(List<OrderTable> orderTableIds) {
        this.orderTables.addAll(this, orderTableIds);
    }

    public void ungroup() {
        List<Long> orderTableIds = orderTables.getIds();
        orderTables.clear();
        registerEvent(new TableGroupUngroupedEvent(orderTableIds));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.get();
    }
}

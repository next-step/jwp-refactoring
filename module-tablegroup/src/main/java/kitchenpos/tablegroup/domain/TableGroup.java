package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
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

    protected TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup() {
        this(null);
    }

    public void addOrderTables(List<OrderTable> orderTableIds) {
        this.orderTables.addAll(id, orderTableIds);
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

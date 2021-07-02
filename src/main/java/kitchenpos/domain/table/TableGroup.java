package kitchenpos.domain.table;

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

    private LocalDateTime createdDate;

    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(id, createdDate, new OrderTables(orderTables));
    }

    public TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup create(TableGroupCreate tableGroupCreate, OrderTables orderTables) {
        if (tableGroupCreate.sizeOfOrderTableIds() != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        if (orderTables.isBookedAny()) {
            throw new IllegalArgumentException();
        }

        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now(), orderTables);

        orderTables.bookedBy(tableGroup);

        return  tableGroup;
    }

    public void ungroup() {
        if (!orderTables.isUnGroupable()) {
            throw new IllegalStateException();
        }

        orderTables.ungroup();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.toCollection();
    }
}

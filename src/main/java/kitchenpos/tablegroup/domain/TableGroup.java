package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.ordertable.domain.OrderTable;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;
    protected TableGroup() {
    }

    private TableGroup(List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
        this.orderTables.assignOrderTables(this);
        createdDate = LocalDateTime.now();
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

}

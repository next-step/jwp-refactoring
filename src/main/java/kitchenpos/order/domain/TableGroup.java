package kitchenpos.order.domain;

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
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    private TableGroup(OrderTables orderTables) {
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
        orderTables.group(this);
    }

    public static TableGroup from(OrderTables orderTables) {
        return new TableGroup(orderTables);
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
        orderTables.ungroup();
    }
}

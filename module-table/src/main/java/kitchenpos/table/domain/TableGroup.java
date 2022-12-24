package kitchenpos.table.domain;

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
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(OrderTables orderTables) {
        this.createdDate = LocalDateTime.now();
        orderTables.mapTableGroup(this);
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void upGroup() {
        this.orderTables.unGroup();
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.getOrderTableIds();
    }

    public List<OrderTable> getOrderTables() {
        return this.orderTables.getOrderTables();
    }
}

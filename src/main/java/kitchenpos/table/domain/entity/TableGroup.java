package kitchenpos.table.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.order.domain.value.OrderTables;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate = LocalDateTime.now();

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
    }

    public TableGroup(OrderTables orderTables) {
        this.orderTables = orderTables;
        orderTables.toTableGroup(this);
    }

    public TableGroup(Long id, OrderTables orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

}

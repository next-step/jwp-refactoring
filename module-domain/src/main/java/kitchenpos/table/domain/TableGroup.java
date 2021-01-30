package kitchenpos.table.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(List<OrderTable> orderTables) {
        this.createdDate = LocalDateTime.now();
        this.orderTables = new OrderTables(orderTables);
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this(orderTables);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public void unGroup() {
        orderTables.empty();
        orderTables = new OrderTables();
    }
}

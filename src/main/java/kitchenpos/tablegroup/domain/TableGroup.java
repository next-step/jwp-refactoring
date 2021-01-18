package kitchenpos.tablegroup.domain;


import kitchenpos.ordertable.domain.OrderTable;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables, this);
    }

    public TableGroup(final Long id, final List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = new OrderTables(orderTables, this);;
    }

    public void ungroup() {
        orderTables.ungroup();
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
}

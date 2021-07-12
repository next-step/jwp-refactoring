package kitchenpos.table.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "table_group")
@Entity
public class OrderTableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected OrderTableGroup() {
    }

    private OrderTableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private OrderTableGroup(OrderTables orderTables) {
        this(null, LocalDateTime.now(), orderTables);
        orderTables.groupBy(this);
    }

    public static OrderTableGroup createWithMapping(OrderTables orderTables) {
        return new OrderTableGroup(orderTables);
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    public void validateNotCompletionStatus() {
        orderTables.validateNotCompletionStatus();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getUnmodifiableList();
    }

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTableIds();
    }
}

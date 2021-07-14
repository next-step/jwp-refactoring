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

    @Embedded
    private OrderTables orderTables;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    protected OrderTableGroup() {
    }

    private OrderTableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static OrderTableGroup of(List<OrderTable> orderTables) {
        return new OrderTableGroup(null, LocalDateTime.now(), OrderTables.of(orderTables));
    }

    public static OrderTableGroup of(Long id, List<OrderTable> orderTables) {
        return new OrderTableGroup(id, LocalDateTime.now(), OrderTables.of(orderTables));
    }

    public void grouped(){
        orderTables.groupBy(getId());
    }

    public void ungrouped() {
        orderTables.ungroup();
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

package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {}

    public TableGroup(OrderTables orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this(orderTables);
        this.id = id;
        this.createdDate = createdDate;
    }

    public void ungroup(List<Order> orders) {
        orders.forEach(Order::checkCookingOrMeal);
        this.orderTables.ungroup();
    }

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTableIds();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public void group() {
        orderTables.group(this);
    }
}

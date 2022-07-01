package kitchenpos.domain;

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
    }

    public TableGroup(List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void createdDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.elements();
    }

    public void changeOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }
}

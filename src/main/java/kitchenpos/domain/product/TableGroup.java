package kitchenpos.domain.product;

import kitchenpos.domain.order.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;


    private TableGroup() {
    }

    private TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    private TableGroup(Long id, OrderTables orderTables) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
//        orderTables.registerTableGroup(this);
    }

    public static TableGroup of(Long id, LocalDateTime createdDate) {
        return new TableGroup(id, createdDate);
    }

    public static TableGroup of(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, OrderTables.from(orderTables));
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroup that = (TableGroup) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

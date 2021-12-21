package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables;

    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    private TableGroup(List<OrderTable> orderTables, LocalDateTime createdDate) {
        this.orderTables = OrderTables.of(orderTables);
        this.orderTables.changeTableGroup(this);
        this.createdDate = createdDate;
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(orderTables, LocalDateTime.now());
    }

    public void ungroup(Orders orders) {
        orderTables.ungroup(orders);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (Objects.isNull(id)) {
            return false;
        }

        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

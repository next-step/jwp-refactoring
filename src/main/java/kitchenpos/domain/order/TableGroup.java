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

    private static final int ORDER_TABLES_MIN_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        validOrderTablesSize(orderTables.size());
        this.orderTables = OrderTables.of(orderTables);
        this.orderTables.changeTableGroup(this);
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    public void ungroup(List<Order> orders) {
        orderTables.ungroup(orders);
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

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = OrderTables.of(orderTables);
    }

    private void validOrderTablesSize(int size) {
        if (size < ORDER_TABLES_MIN_SIZE) {
            throw new IllegalArgumentException(
                String.format("단체 지정에 속하는 주문테이블은 %s개 이상이어야 합니다.", ORDER_TABLES_MIN_SIZE));
        }
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

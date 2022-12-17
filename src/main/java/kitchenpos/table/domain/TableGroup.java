package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "table_group")
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {}

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.orderTables = OrderTables.from(orderTables);
        groupingOrderTables();
    }

    private void groupingOrderTables() {
        orderTables.grouping(id);
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

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTableIds();
    }

    public List<OrderTable> getOrderTablesReadOnlyValue() {
        return orderTables.readOnlyValue();
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    public static class Builder {

        private Long id;
        private List<OrderTable> orderTables;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder orderTables(List<OrderTable> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(id, orderTables);
        }
    }
}

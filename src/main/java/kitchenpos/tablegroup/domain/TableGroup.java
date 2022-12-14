package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

@Entity
@Table(name = "TABLE_GROUP")
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
        setOrderTablesTableGourp();
        changeOrderTablesIsNotEmpty();
    }

    private void setOrderTablesTableGourp() {
        orderTables.setTableGroup(this);
    }

    private void changeOrderTablesIsNotEmpty() {
        orderTables.changeIsNotEmpty();
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

    public List<OrderTable> getOrderTablesReadOnlyValue() {
        return orderTables.readOnlyValue();
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

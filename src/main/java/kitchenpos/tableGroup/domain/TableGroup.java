package kitchenpos.tableGroup.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    private LocalDateTime createdDate;

    public static TableGroup of(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(null, orderTables);
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables.addAll(orderTables);
        this.createdDate = LocalDateTime.now();
    }

    protected TableGroup() {
    }

    public void unGroup() {
        orderTables.unGroup();
    }

    public Long getId() {
        return id;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}

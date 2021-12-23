package kitchenpos.table.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
        this.orderTables.addAll(with(orderTables));
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

    public List<OrderTable> getOrderTable() {
        return orderTables.getList();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getList();
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    private List<OrderTable> with(List<OrderTable> orderTables) {
        orderTables.stream()
            .forEach(orderTable -> {
                if (this.orderTables.contains(orderTable)) {
                    return;
                }
                orderTable.withTableGroup(this);
            });
        return orderTables;
    }

}

package kitchenpos.table.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "table_group")
@EntityListeners(AuditingEntityListener.class)
public class TableGroup extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long id;
    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.values();
    }

    public List<Long> getOrderTableIds() {
        return orderTables.values().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void enGroup(TableGroupValidator validator) {
        validator.validateEnGroup(orderTables.values());

        for (OrderTable orderTable : orderTables.values()) {
            orderTable.enGroupBy(this.id);
        }
    }

    public void unGroup(TableGroupValidator validator) {
        validator.validateUnGroup(getOrderTableIds());

        for (OrderTable orderTable : orderTables.values()) {
            orderTable.unGroupBy();
        }
        orderTables.removeAll();
    }
}

package kitchenpos.table_group.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    private static final int MIN_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public TableGroup(Long id) {
        this.id = id;
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

    public void group(List<OrderTable> orderTables) {
        checkAddable(orderTables);
        this.orderTables.addAll(this, orderTables);
    }

    private void checkAddable(List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_TABLE_SIZE) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            checkEmptyOrGrouped(orderTable);
        }
    }

    private void checkEmptyOrGrouped(OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    public List<Long> getTableIds() {
        return orderTables.getIds();
    }
}

package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.table.exception.ExistNonEmptyOrderTableException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
    }

    public TableGroup(Long id) {
        this.id = id;
    }

    public TableGroup(OrderTables orderTables) {
        validationOrderTables(orderTables);
        this.orderTables = orderTables;
        orderTables.toTableGroup(this);
    }

    private void validationOrderTables(OrderTables orderTables) {
        if (isExistEmptyOrderTable(orderTables)) {
            throw new ExistNonEmptyOrderTableException();
        }
    }

    private boolean isExistEmptyOrderTable(OrderTables orderTables) {
        return orderTables.getOrderTables().stream()
            .anyMatch(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup()));
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = new OrderTables(orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void upgroup() {
        orderTables.upgroup();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }
}

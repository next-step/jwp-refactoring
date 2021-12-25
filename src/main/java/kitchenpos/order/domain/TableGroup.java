package kitchenpos.order.domain;

import kitchenpos.order.application.exception.InvalidTableState;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        orderTables.forEach(this::validate);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new OrderTables(orderTables);
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    private void validate(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidTableState("빈 테이블을 일행으로 지정할 수 없습니다.");
        }
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new InvalidTableState("테이블에 일행이 있습니다.");
        }
    }

    public void validateTableState() {
        orderTables.validateTableState();
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
}

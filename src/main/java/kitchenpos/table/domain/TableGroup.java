package kitchenpos.table.domain;

import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup extends AbstractAggregateRoot<TableGroup> {
    private static final int MIN_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables.addAll(this, orderTables);
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_SIZE) {
            throw new InvalidParameterException("테이블 갯수가 적습니다.");
        }
        if (hasNotEmptyOrGrouped(orderTables)) {
            throw new IllegalStateException(
                "테이블 중에 빈 테이블이 아니거나 다른 단체에 지정된 테이블이 있습니다.");
        }
    }

    private boolean hasNotEmptyOrGrouped(List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(it -> !it.isEmpty() || it.isGrouped());
    }

    public void ungroup() {
        List<Long> orderTableIds = orderTables.getIds();
        orderTables.clear();
        registerEvent(new TableGroupUngroupedEvent(orderTableIds));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.get();
    }
}

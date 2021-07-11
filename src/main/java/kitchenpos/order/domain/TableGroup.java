package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private final OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public TableGroup(final OrderTables orderTables) {
        validateOrderTables(orderTables);
        this.orderTables.addAll(orderTables.toList());
        this.orderTables.updateTableGroup(this);
    }

    private void validateOrderTables(final OrderTables orderTables) {
        if (isNotSatisfyToTableGroup(orderTables)) {
            throw new IllegalArgumentException("이미 사용 중이거나 다른 그룹에 속한 테이블은 그룹으로 설정할 수 없습니다.");
        }
    }

    private boolean isNotSatisfyToTableGroup(final OrderTables orderTables) {
        return orderTables.hasOccupiedTable() || orderTables.hasTableGroupId();
    }

    public Long getId() {
        return id;
    }

    public void changeId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.toList();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final TableGroup that = (TableGroup)o;
        return Objects.equals(id, that.id) && Objects.equals(createdDate, that.createdDate)
            && Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, orderTables);
    }
}

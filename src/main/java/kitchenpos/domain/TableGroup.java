package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

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

    public TableGroup(OrderTables orderTables) {
        validateOrderTables(orderTables);
        this.orderTables.addAll(orderTables.toList());
        this.orderTables.updateTableGroup(this);
    }

    private void validateOrderTables(OrderTables orderTables) {
        if (isNotSatisfyToTableGroup(orderTables)) {
            throw new IllegalArgumentException("이미 사용 중이거나 다른 그룹에 속한 테이블은 그룹으로 설정할 수 없습니다.");
        }
    }

    private boolean isNotSatisfyToTableGroup(OrderTables orderTables) {
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
}

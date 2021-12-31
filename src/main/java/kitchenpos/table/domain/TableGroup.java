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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;
    
    protected TableGroup() {
        this.orderTables = new OrderTables();
    }
    
    private TableGroup(List<OrderTable> orderTables) {
        this.orderTables = new OrderTables();
        addOrderTables(orderTables);
    }
    
    public static TableGroup from(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
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

    public void validateOrderTables() {
        if (CollectionUtils.isEmpty(orderTables.getOrderTables()) || orderTables.count() < 2) {
            throw new IllegalArgumentException("단체지정은 최소 두 테이블 이상만 가능합니다");
        }
    }
    
    public void ungroup() {
        orderTables.ungroup();
    }
    
    private void addOrderTables(final List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> {
            orderTable.setTableGroup(this);
            this.orderTables.add(orderTable);
        });
        validateOrderTables();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

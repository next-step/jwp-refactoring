package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.ordertable.domain.OrderTable;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint(20)")
    private Long id;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
    }

    public TableGroup(Long id) {
        this.id = id;
    }

    public TableGroup(OrderTables orderTables) {
        this(null, orderTables);
    }

    public TableGroup(Long id, OrderTables orderTables) {
        orderTables.group(this);
        this.id = id;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.value();
    }

    private void validateOrderIsCompletion() {
        if (!isOrderCompletion()) {
            throw new IllegalArgumentException("아직 테이블의 주문이 계산완료되지 않았습니다");
        }
    }

    private boolean isOrderCompletion() {
        return orderTables.isOrderCompletion();
    }

    public void ungrouped() {
        validateOrderIsCompletion();
        ungroupOrderTable();

    }

    private void ungroupOrderTable() {
        orderTables.ungroup();
    }
}

package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.order.domain.OrderTables;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(OrderTables orderTables) {
        this.orderTables = orderTables;
        this.createdDate = LocalDateTime.now();
        validateEqualOrderTableSize(orderTables.size());
        updateOrderTables(orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public void updateOrderTables(OrderTables orderTables) {
        orderTables.updateTableGroup(this.id);
        this.orderTables = orderTables;
    }

    public void validateEqualOrderTableSize(int size) {
        if (orderTables.size() != size) {
            throw new OrderTableException("요청 주문 테이블 사이즈와 저장된 주문 테이블 사이즈가 다릅니다");
        }
    }

    @Override
    public String toString() {
        return "TableGroup{" +
            "id=" + id +
            ", createdDate=" + createdDate +
            ", orderTables=" + orderTables +
            '}';
    }
}

package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.Collection;
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

    public TableGroup() { }

    public TableGroup(Collection<OrderTable> orderTables) {
        orderTables.forEach(this::addOrderTable);
        verifyOrderTablesSize();
    }

    private void verifyOrderTablesSize() {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블이 2개 이상인 경우에만 단체 지정이 가능합니다.");
        }
    }

    public void addOrderTable(OrderTable orderTable) {

         if (!orderTable.isEmpty() || orderTable.getTableGroupId() != null) {
            throw new IllegalArgumentException("주문 테이블이 빈 상태이거나 주문 테이블이 이미 단체 지정되어 있습니다.");
        }

        orderTable.notEmpty();
        orderTable.group(this);
        orderTables.add(orderTable);
    }

    public void ungroup() {
        orderTables.ungroup();
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
}

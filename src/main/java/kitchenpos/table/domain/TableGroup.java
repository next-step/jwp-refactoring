package kitchenpos.table.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    private static final String ORDER_TABLE_LEAST_2 = "주문 테이블은 최소 2개 이상이어야 합니다";
    private static final int MINIMUM_GROUP_SIZE = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @Embedded
    private final OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_GROUP_SIZE) {
            throw new IllegalArgumentException(ORDER_TABLE_LEAST_2);
        }
        orderTables.forEach(orderTable -> orderTable.groupBy(this));
    }

    public static TableGroup group(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
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

    public void ungroup() {
        orderTables.ungroup();
    }
}

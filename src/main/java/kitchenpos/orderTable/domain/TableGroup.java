package kitchenpos.orderTable.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {
    public static final int ORDER_TABLE_REQUEST_MIN = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    private TableGroup(OrderTables orderTables, List<Long> orderTableIds) {
        validateOrderTableIds(orderTableIds);
        validateOrderTablesSize(orderTables, orderTableIds.size());
        orderTables.addTableGroup(this);
        orderTables.reserve();
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    public static TableGroup from(OrderTables orderTables, List<Long> orderTableIds) {
        return new TableGroup(orderTables, orderTableIds);
    }

    public Long id() {
        return id;
    }

    public List<OrderTable> readOnlyOrderTables() {
        return orderTables.readOnlyOrderTables();
    }

    public LocalDateTime createdDate() {
        return createdDate;
    }


    private void validateOrderTableIds(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < ORDER_TABLE_REQUEST_MIN) {
            throw new IllegalArgumentException(ORDER_TABLE_REQUEST_MIN + "이상 주문테이블이 필요합니다.");
        }
    }

    private void validateOrderTablesSize(OrderTables orderTables, int size) {
        if (orderTables.isNotEqualSize(size)) {
            throw new IllegalArgumentException("비교하는 수와 주문 테이블의 수가 일치하지 않습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

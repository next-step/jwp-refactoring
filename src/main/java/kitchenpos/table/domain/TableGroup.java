package kitchenpos.table.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "table_group")
@EntityListeners(AuditingEntityListener.class)
public class TableGroup extends BaseTime {
    private static final int GROUP_TABLE_MIN_SIZE = 2;
    private static final String EXCEPTION_MESSAGE_MIN_GROUP_TABLE = "단체 테이블은 테이블" + GROUP_TABLE_MIN_SIZE + "개 이상 필요로 합니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long id;
    @Embedded
    private final OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        enGroup(orderTables);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.values();
    }

    private void enGroup(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        for (OrderTable orderTable : orderTables) {
            addOrderTable(orderTable);
        }
    }

    public void unGroup() {
        for (OrderTable orderTable : orderTables.values()) {
            orderTable.validateOrderStatus();
            orderTable.unGroupBy();
        }
        orderTables.removeAll();
    }

    private void addOrderTable(OrderTable orderTable) {
        orderTable.enGroupBy(this);
        this.orderTables.add(orderTable);
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < GROUP_TABLE_MIN_SIZE) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_MIN_GROUP_TABLE);
        }
    }
}

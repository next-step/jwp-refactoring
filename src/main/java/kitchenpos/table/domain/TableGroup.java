package kitchenpos.table.domain;

import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "table_group")
public class TableGroup {
    private static final int MINIMUM_SIZE_OF_GROUP = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
        this(null, OrderTables.create());
    }

    public TableGroup(Long id, OrderTables orderTables) {
        this(id, LocalDateTime.now(), orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        validateOrderTables(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(Long id, OrderTables orderTables) {
        return new TableGroup(id, orderTables);
    }

    private void validateOrderTables(OrderTables orderTables) {
        if (orderTables.isEmpty()) {
            return;
        }
        validateOrderTableCount(orderTables);
        validateEmptyTable(orderTables);
        validateGroupedTable(orderTables);
    }

    private void validateOrderTableCount(OrderTables orderTables) {
        if (!orderTables.hasBiggerOrEqualSizeThan(MINIMUM_SIZE_OF_GROUP)) {
            throw new IllegalArgumentException(
                    String.format("단체 지정을 하려면 최소 %d개 이상의 테이블이 필요합니다.", MINIMUM_SIZE_OF_GROUP));
        }
    }

    private void validateEmptyTable(OrderTables orderTables) {
        if (orderTables.hasEmptyOrderTable()) {
            throw new IllegalArgumentException("빈 테이블을 단체 지정할 수 없습니다.");
        }
    }

    private void validateGroupedTable(OrderTables orderTables) {
        if (orderTables.hasGroupedTable()) {
            throw new IllegalArgumentException("이미 단체 지정된 테이블을 단체 지정할 수 없습니다.");
        }
    }

    public void group(OrderTables orderTables) {
        validateOrderTables(orderTables);
        orderTables.group(this);
        this.orderTables = orderTables;
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

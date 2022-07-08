package kitchenpos.table.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "table_group")
@Entity
public class TableGroup {
    private static final int MIN_ORDER_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(null, orderTables);
    }

    public TableGroup(final Long id, final List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        this.id = id;
        orderTables.forEach(this::addOrderTable);
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException(String.format("단체 지정시 테이블은 최소 %d개 이상이어야 합니다.", MIN_ORDER_TABLE_SIZE));
        }
    }

    private void addOrderTable(final OrderTable orderTable) {
        validateOrderTable(orderTable);
        this.createdDate = LocalDateTime.now();
        this.orderTables.add(orderTable);
        orderTable.addedBy(this);
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (!orderTable.isEmptyTableGroup()) {
            throw new IllegalStateException("이미 단체 지정이 되어있는 테이블은 단체 지정을 할 수 없습니다.");
        }
        if (!orderTable.isEmpty()) {
            throw new IllegalStateException("비어있지 않은 테이블은 단체 지정을 할 수 없습니다.");
        }
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTableIds();
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

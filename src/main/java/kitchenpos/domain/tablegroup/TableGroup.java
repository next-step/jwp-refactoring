package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

import kitchenpos.domain.table.OrderTable;

@Entity
@Table(name = "table_group")
public class TableGroup {
    private static final String INVALID_GROUP_ORDER_TABLE_COUNT = "TableGroup 은 최소 2개 이상의 OrderTable 이 존재해야합니다.";
    private static final String INVALID_GROUP_ORDER_TABLE = "OrderTable 은 TableGroup 이 할당되지 않으면서 비어있어야 합니다.";
    private static final int MIN_ORDER_TABLE_COUNT = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {}

    private TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    private TableGroup(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup from(Long id) {
        return new TableGroup(id);
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        validateDoGroupOrderTables(orderTables);
        return new TableGroup(orderTables);
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
        orderTables.forEach(orderTable -> orderTable.alignTableGroup(this));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    private static void validateDoGroupOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_COUNT) {
            throw new IllegalArgumentException(INVALID_GROUP_ORDER_TABLE_COUNT);
        }

        for (final OrderTable orderTable : orderTables) {
            validateDoGroupOrderTable(orderTable);
        }
    }

    private static void validateDoGroupOrderTable(OrderTable orderTable) {
        if (!orderTable.isEmpty() || orderTable.hasTableGroup()) {
            throw new IllegalArgumentException(INVALID_GROUP_ORDER_TABLE);
        }
    }
}

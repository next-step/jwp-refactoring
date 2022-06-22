package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.CreateTableGroupException;

@Entity
@Table(name = "table_group")
public class TableGroup {

    public static final int MIN_ORDER_TABLE_COUNT = 2;
    public static final String CREATE_TABLE_GROUP_DEFAULT_RULE = "테이블이 없거나 1개 이하인 경우에는 단체를 생성할 수 없습니다.";
    public static final String CREATE_TABLE_GROUP_ORDER_TABLE_MISMATCH = "요청한 테이블 정보와 현재 테이블 정보가 일치하지 않습니다.";
    public static final String CREATE_TABLE_GROUP_ORDER_TABLE_STATUS = "단체로 지정할 테이블이 빈 테이블이거나 이미 단체가 지정된 경우 단체를 생성할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables = OrderTables.createEmpty();

    protected TableGroup() {}

    private TableGroup(List<OrderTable> orderTables) {
        this.orderTables = OrderTables.from(orderTables);
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = OrderTables.from(orderTables);
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        validateTableGroup(orderTables);
        return new TableGroup(orderTables);
    }

    public static TableGroup of(Long id, List<OrderTable> orderTables) {
        validateTableGroup(orderTables);
        return new TableGroup(id, orderTables);
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

    public List<OrderTable> findOrderTables() {
        return this.orderTables.getReadOnlyValues();
    }

    public void assignedOrderTables(List<OrderTable> orderTables) {
        this.orderTables = OrderTables.from(orderTables);
        orderTables.forEach(orderTable -> orderTable.mappedByTableGroup(this));
    }

    private static void validateTableGroup(List<OrderTable> orderTables) {
        if (orderTables == null || orderTables.isEmpty()) {
            throw new CreateTableGroupException(CREATE_TABLE_GROUP_DEFAULT_RULE);
        }

        if (orderTables.size() < MIN_ORDER_TABLE_COUNT) {
            throw new CreateTableGroupException(CREATE_TABLE_GROUP_DEFAULT_RULE);
        }

        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.getTableGroup() != null) {
                throw new CreateTableGroupException(CREATE_TABLE_GROUP_ORDER_TABLE_STATUS);
            }
        }
    }
}

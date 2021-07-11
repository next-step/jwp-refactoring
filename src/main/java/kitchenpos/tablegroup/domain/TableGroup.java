package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    private static final String INVALID_ORDER_TABLE = "유효하지 않은 주문 테이블";
    private static final String TABLE_SIZE_NOT_MATCH = "테이블 갯수가 일치하지 않습니다";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables;

    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    private TableGroup(Long id, OrderTables orderTables, LocalDateTime createdDate) {
        this.id = id;
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    public static TableGroup createWithIdValidation(List<Long> orderIds, OrderTables orderTables) {
        validateOrderTables(orderIds, orderTables);
        TableGroup tableGroup = new TableGroup(null, orderTables, LocalDateTime.now());
        orderTables.assignTableGroup(tableGroup);
        return tableGroup;
    }

    public static TableGroup of(OrderTables orderTables) {
        if (orderTables.checkTableSizeNotEnough()) {
            throw new IllegalArgumentException(INVALID_ORDER_TABLE);
        }
        return new TableGroup(null, orderTables, LocalDateTime.now());
    }

    private static void validateOrderTables(List<Long> orderIds, OrderTables orderTables) {
        if (orderTables.checkTableSizeNotEnough()) {
            throw new IllegalArgumentException(INVALID_ORDER_TABLE);
        }
        if (orderIds.size() != orderTables.size()) {
            throw new IllegalArgumentException(TABLE_SIZE_NOT_MATCH);
        }
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTableIds();
    }

    public void ungroup() {
        orderTables.upgroup();
    }
}

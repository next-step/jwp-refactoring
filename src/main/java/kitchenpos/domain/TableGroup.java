package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {
    private static final int MIN_TABLE_COUNT = 2;
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;
    private OrderTables orderTables2;

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, null);
    }

    public TableGroup(OrderTables orderTables, LocalDateTime createdDate) {
        validateMinTableCount(orderTables);
        validateNotEmptyTables(orderTables);
        if (orderTables.containsGroupedOrderTables()) {
            throw new IllegalArgumentException("비어있지 않거나, 이미 그룹화되어 있는 테이블은 그룹화 할 수 없습니다.");
        }
        this.orderTables2 = orderTables;
        this.orderTables2.groupBy(this);
        this.createdDate = createdDate;
    }

    private void validateNotEmptyTables(OrderTables orderTables) {
        if (orderTables.containsNotEmptyTable()) {
            throw new IllegalArgumentException("비어있지 않거나, 이미 그룹화되어 있는 테이블은 그룹화 할 수 없습니다.");
        }
    }

    private void validateMinTableCount(OrderTables orderTables) {
        if (orderTables.size() < MIN_TABLE_COUNT) {
            throw new IllegalArgumentException("2개 미만의 주문테이블은 그룹화 할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}

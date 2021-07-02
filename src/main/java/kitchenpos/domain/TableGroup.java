package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {
    private static final int MIN_TABLE_COUNT = 2;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {}

    public TableGroup(OrderTables orderTables, LocalDateTime createdDate) {
        validate(orderTables);
        this.orderTables = orderTables;
        this.orderTables.groupBy(this);
        this.createdDate = createdDate;
    }

    public TableGroup(List<OrderTable> orderTables, LocalDateTime createdDate) {
        this.orderTables = requireValidOrderTables(orderTables);
        this.orderTables.groupBy(this);
        this.createdDate = createdDate;
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

    private void validate(OrderTables orderTables) {
        validateMinTableCount(orderTables);
        validateNotEmptyTables(orderTables);
        validateNoGroupedTables(orderTables);
    }

    private OrderTables requireValidOrderTables(List<OrderTable> tables) {
        OrderTables orderTables = OrderTables.of(tables);
        validate(orderTables);
        return orderTables;
    }

    private void validateNoGroupedTables(OrderTables orderTables) {
        if (orderTables.containsGroupedOrderTables()) {
            throw new IllegalArgumentException("비어있지 않거나, 이미 그룹화되어 있는 테이블은 그룹화 할 수 없습니다.");
        }
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

    public List<Long> getOrderTableIds() {
        return this.orderTables.getOrderTableIds();
    }

    public void ungroup() {
        orderTables.ungrouped();
    }
}

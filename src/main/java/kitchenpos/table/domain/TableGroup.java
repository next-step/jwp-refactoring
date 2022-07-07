package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {

    public static final String CONTAINS_IS_NOT_EMPTY_ORDER_TABLE_ERROR_MESSAGE = "빈 좌석이 아닌 테이블이 포함되어 있습니다.";
    public static final String CONTAINS_ALREADY_GROPING_ORDER_TABLE_ERROR_MESSAGE = "이미 단체 지정된 주문 테이블이 포함 되어 있습니다.";
    public static final String NOT_EXISTS_GROUPING_TARGET_ORDER_CONTAINS_ERROR_MESSAGE = "존재하지 않는 주문 테이블이 포함되어 있습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {

    }

    private TableGroup(List<OrderTable> orderTables) {
        validateGroupingTargetOrderTables(orderTables);
        this.createdDate = LocalDateTime.now();
        this.orderTables = new OrderTables(orderTables);
        this.orderTables.allocateAll(this);
    }

    public static TableGroup of(List<Long> orderTablesIds, List<OrderTable> groupingTargetOrderTables) {
        validateGroupingTargetOrderTables(orderTablesIds, groupingTargetOrderTables);
        return new TableGroup(groupingTargetOrderTables);
    }

    public static void validateGroupingTargetOrderTables(
        final List<Long> orderTableIds,
        final List<OrderTable> groupingTargetOrderTables
    ) {
        if (orderTableIds.size() != groupingTargetOrderTables.size()) {
            throw new IllegalArgumentException(NOT_EXISTS_GROUPING_TARGET_ORDER_CONTAINS_ERROR_MESSAGE);
        }
    }

    private void validateGroupingTargetOrderTables(final List<OrderTable> gropingTargetTables) {
        if (isContainsAlreadyGroupingOrderTable(gropingTargetTables)) {
            throw new IllegalArgumentException(CONTAINS_ALREADY_GROPING_ORDER_TABLE_ERROR_MESSAGE);
        }
        if (isContainsNotEmptyOrderTable(gropingTargetTables)) {
            throw new IllegalArgumentException(CONTAINS_IS_NOT_EMPTY_ORDER_TABLE_ERROR_MESSAGE);
        }
    }

    private boolean isContainsNotEmptyOrderTable(final List<OrderTable> gropingTargetTables) {
        return gropingTargetTables.stream()
            .anyMatch(it -> !it.isEmpty());
    }

    private boolean isContainsAlreadyGroupingOrderTable(final List<OrderTable> gropingTargetTables) {
        return gropingTargetTables.stream()
            .anyMatch(OrderTable::isGroupTable);
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public List<Long> getOrderTablesIds(){
        return orderTables.getOrderTablesIds();
    }

    public void deallocateOrderTable() {
        orderTables.deallocateAll();
    }

    public Long getId() {
        return id;
    }
}

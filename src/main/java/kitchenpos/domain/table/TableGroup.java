package kitchenpos.domain.table;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {

    public static final String CONTAINS_IS_NOT_EMPTY_ORDER_TABLE_ERROR_MESSAGE = "빈 좌석이 아닌 테이블이 포함되어 있습니다.";
    public static final String CONTAINS_ALREADY_GROPING_ORDER_TABLE_ERROR_MESSAGE = "이미 단체 지정된 주문 테이블이 포함 되어 있습니다.";
    public static final String NOT_EXISTS_GROUPING_TARGET_ORDER_CONTAINS_ERROR_MESSAGE = "존재하지 않는 주문 테이블이 포함되어 있습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY)
    private List<OrderTable> orderTables;

    public TableGroup() {

    }

    public TableGroup(List<OrderTable> orderTables) {
        validateGroupingTargetOrderTables(orderTables);
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
        orderTables.forEach(it -> it.allocateTableGroup(this));
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
            throw new IllegalArgumentException(CONTAINS_IS_NOT_EMPTY_ORDER_TABLE_ERROR_MESSAGE);
        }
        if (isContainsNotEmptyOrderTable(gropingTargetTables)) {
            throw new IllegalArgumentException(CONTAINS_ALREADY_GROPING_ORDER_TABLE_ERROR_MESSAGE);
        }
    }

    private boolean isContainsNotEmptyOrderTable(final List<OrderTable> gropingTargetTables) {
        return gropingTargetTables.stream()
            .anyMatch(it -> !it.isEmpty());
    }

    private boolean isContainsAlreadyGroupingOrderTable(final List<OrderTable> gropingTargetTables) {
        return gropingTargetTables.stream()
            .anyMatch(it -> it.isGroupTable());
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

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}

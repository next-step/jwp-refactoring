package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroupId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        validateSize(orderTables);
        validateIsEmpty(orderTables);
        validateNonNullTableGroup(orderTables);
        this.orderTables = orderTables;
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public static OrderTables of(List<OrderTable> orderTables, List<Long> orderTableIds) {
        validateDifferent(orderTables, orderTableIds);
        return new OrderTables(orderTables);
    }

    public void group(Long tableGroupId) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.groupByTableGroupId(tableGroupId);
        }
    }

    public void unGroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
        }
    }

    public List<OrderTable> getValue() {
        return Collections.unmodifiableList(orderTables);
    }

    private void validateSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정 할 테이블 목록의 개수는 2개 이상이어야 합니다.");
        }
    }

    private void validateIsEmpty(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty()) {
                throw new IllegalArgumentException("단체 지정 할 모든 테이블은 빈 테이블이 아니어야 한다");
            }
        }
    }

    private void validateNonNullTableGroup(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (orderTable.isGroupedByTableGroup()) {
                throw new IllegalArgumentException("단체 지정 할 모든 테이블은 단체 지정 되지 않은 테이블이어야 합니다.");
            }
        }
    }

    private static void validateDifferent(List<OrderTable> orderTables, List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("모든 테이블은 존재하는 테이블이어야 합니다.");
        }
    }
}

package kitchenpos.table.domain;

import static kitchenpos.Exception.TableGroupSizeException.TABLE_GROUP_SIZE_EXCEPTION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.Exception.OrderTableAlreadyEmptyException;
import kitchenpos.Exception.OrderTableAlreadyTableGroupException;
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
            throw TABLE_GROUP_SIZE_EXCEPTION;
        }
    }

    private void validateIsEmpty(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty()) {
                throw new OrderTableAlreadyEmptyException("단체 지정 할 모든 테이블은 빈 테이블이 아니어야 한다");
            }
        }
    }

    private void validateNonNullTableGroup(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (orderTable.isGroupedByTableGroup()) {
                throw new OrderTableAlreadyTableGroupException("단체 지정 할 모든 테이블은 단체 지정 되지 않은 테이블이어야 합니다.");
            }
        }
    }
}

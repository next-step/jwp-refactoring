package kitchenpos.ordertable.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static kitchenpos.common.ErrorMessage.*;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        validateIsCreatableTableGroup(orderTables);
    }

    private void validateIsCreatableTableGroup(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException(NEED_TWO_ORDER_TABLE.getMessage());
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException(VALIDATION_OF_GROUP.getMessage());
            }
        }
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> value() {
        return orderTables;
    }

    public int size() {
        return orderTables.size();
    }

    public void group(final Long tableGroupId) {
        orderTables.forEach(orderTable -> orderTable.group(tableGroupId));
    }

    public void unGroup() {
        orderTables.forEach(OrderTable::unGroup);
    }

    public List<OrderTable> findOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }
}

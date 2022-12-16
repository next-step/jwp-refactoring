package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kitchenpos.common.ErrorMessage.NEED_TWO_ORDER_TABLE;
import static kitchenpos.common.ErrorMessage.VALIDATION_OF_GROUP;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
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
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException(VALIDATION_OF_GROUP.getMessage());
            }
        }
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public void group(final TableGroup tableGroup) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.group(tableGroup);
        }
    }

    public List<OrderTable> value() {
        return orderTables;
    }
}

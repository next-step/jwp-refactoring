package kitchenpos.table.domain;

import kitchenpos.common.exception.InvalidOrderTableException;
import kitchenpos.common.exception.InvalidTableGroupSizeException;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderTables {
    private static final int MIN_TABLE_SIZE = 2;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.MERGE)
    private final List<OrderTable> orderTables;

    protected OrderTables(){
        this.orderTables = new ArrayList<>();
    }

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void validateOrderTable() {
        if (isInvalidTableSize()) {
            throw new InvalidTableGroupSizeException();
        }
        if (validateOrderTables()) {
            throw new InvalidOrderTableException();
        }
    }

    private boolean isInvalidTableSize() {
        return CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLE_SIZE;
    }

    private boolean validateOrderTables() {
        return orderTables.stream()
                .anyMatch(this::isNotEmptyOrNonNullTableGroup);
    }

    private boolean isNotEmptyOrNonNullTableGroup(final OrderTable orderTable) {
        return !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup());
    }

    public void initTableGroup(final TableGroup tableGroup) {
        orderTables.forEach(tableGroup::initOrderTables);
    }

    public int size() {
        return orderTables.size();
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::unTableGroup);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}

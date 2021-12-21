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

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        validate();
    }

    private void validate() {
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

    private boolean isNotEmptyOrNonNullTableGroup(OrderTable orderTable) {
        return !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup());
    }

    public void initTableGroup(TableGroup tableGroup) {
        orderTables.forEach(tableGroup::initOrderTables);
    }

    public int size() {
        return orderTables.size();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}

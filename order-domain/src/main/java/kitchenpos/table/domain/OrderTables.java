package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public boolean sameSizeAs(int size) {
        return orderTables.size() == size;
    }

    private void validate(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void toGroup(TableValidator validator, TableGroup tableGroup) {
        validator.validateTableGroupCreatable(this);
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(validator, false);
            orderTable.toGroup(tableGroup);
        }
        tableGroup.setOrderTables(this);
    }

    public void ungroup(TableValidator validator) {
        validator.validateUngroupPossible(this);
        for (final OrderTable orderTable : orderTables) {
            orderTable.toGroup(null);
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }
}

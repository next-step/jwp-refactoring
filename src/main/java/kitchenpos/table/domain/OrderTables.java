package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.table.exception.OrderUsingException;
import kitchenpos.tablegroup.domain.TableGroup;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private final List<OrderTable> orderTables;

    protected OrderTables() {
        orderTables = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public Boolean avaliableTable() {
        return !orderTables.stream().anyMatch(OrderTable::isAvaliableTable);
    }

    public void chargedBy(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.chargedBy(tableGroup);
        }
    }

    public void ungroup() {
        validation();
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validation() {
        if (isImmutableOrder()) {
            throw new OrderUsingException();
        }
    }

    private Boolean isImmutableOrder() {
        return orderTables.stream().anyMatch(OrderTable::isImmutableOrder);
    }

}

package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.table.exception.OutOfOrderTableException;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    public static final int MIN_ORDER_TABLE_COUNT = 2;

    @OneToMany(mappedBy = "tableGroup", orphanRemoval = true)
    private List<OrderTable> orderTables;

    protected OrderTables() {
        orderTables = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTables) {
        validationOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    private void validationOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_COUNT) {
            throw new OutOfOrderTableException(MIN_ORDER_TABLE_COUNT);
        }
    }

    public void toTableGroup(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.toTableGroup(tableGroup));
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public void upgroup() {
        orderTables.forEach(OrderTable::ungroup);
    }
}

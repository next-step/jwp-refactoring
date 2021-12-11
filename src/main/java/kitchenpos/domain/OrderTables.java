package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    private static final int MIN_REQUEST_ORDER_TABLE_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void group(TableGroup tableGroup) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.group(tableGroup);
        }
    }

    public void checkOrderTables(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MIN_REQUEST_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException();
        }

        if (!isSameSize(orderTableIds)) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.checkNonEmptyInGroup();
        }
    }

    private boolean isSameSize(List<Long> orderTableIds) {
        return orderTables.size() == orderTableIds.size();
    }
}

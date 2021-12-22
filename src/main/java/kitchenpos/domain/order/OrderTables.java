package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.InvalidParameterException;

@Embeddable
public class OrderTables {

    public static final int ORDER_TABLES_MIN_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        validOrderTablesSize(orderTables.size());
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }


    public void changeTableGroup(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.changeTableGroup(tableGroup));
    }

    public void ungroup(Orders orders) {
        orderTables.forEach(
            orderTable -> {
                Orders targetOrders = orders.matchedBy(orderTable);
                orderTable.ungroup(targetOrders);
            });
    }

    private void validOrderTablesSize(int size) {
        if (size < ORDER_TABLES_MIN_SIZE) {
            throw new InvalidParameterException(CommonErrorCode.ORDER_TABLES_MIN_UNDER_EXCEPTION);
        }
    }
}

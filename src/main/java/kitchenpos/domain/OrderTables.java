package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.constant.ErrorCode;

@Embeddable
public class OrderTables {

    private static final int MIN_SIZE = 2;

    //@OneToMany(mappedBy = "table_group")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {}

    private OrderTables(List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    private void validateOrderTableSize(List<OrderTable> orderTables) {
        if(orderTables.isEmpty() || orderTables.size() < MIN_SIZE) {
            throw new IllegalArgumentException(ErrorCode.주문_테이블은_2개_이상여야함.getErrorMessage());
        }
    }

    public void ungroupOrderTables() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> unmodifiableOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }
}

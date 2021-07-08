package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.table.exception.CannotUngroupOrderTableException;
import kitchenpos.table.exception.OutOfOrderTableException;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    public static final String THERE_IS_A_HISTORY_OF_ORDERS_AT_AN_ONGOING = "진행중(조리 or 식사)인 단계의 주문 이력이 존재할 경우 해제가 불가능하다.";
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
        orderTables.forEach(orderTable -> {
            validationOrderTable(orderTable);
            orderTable.ungroup();
        });
    }

    private void validationOrderTable(OrderTable orderTable) {
        if (orderTable.isNotCompleted()) {
            throw new CannotUngroupOrderTableException(THERE_IS_A_HISTORY_OF_ORDERS_AT_AN_ONGOING);
        }
    }
}

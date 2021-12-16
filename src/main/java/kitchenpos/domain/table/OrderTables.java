package kitchenpos.domain.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    private static final String INVALID_GROUP_ORDER_TABLE_COUNT = "최소 2개 이상의 OrderTable 이 존재해야합니다.";
    private static final String INVALID_GROUP_ORDER_TABLE = "OrderTable 은 TableGroup 이 할당되지 않으면서 비어있어야 합니다.";
    private static final int MIN_ORDER_TABLE_COUNT = 2;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables createEmpty() {
        return new OrderTables(new ArrayList<>());
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        validateDoGroupOrderTables(orderTables);
        return new OrderTables(orderTables);
    }

    /******************************************/
    private static void validateDoGroupOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_COUNT) {
            throw new IllegalArgumentException(INVALID_GROUP_ORDER_TABLE_COUNT);
        }

        for (final OrderTable orderTable : orderTables) {
            validateDoGroupOrderTable(orderTable);
        }
    }

    private static void validateDoGroupOrderTable(OrderTable orderTable) {
        if (!orderTable.isEmpty() || orderTable.hasTableGroup()) {
            throw new IllegalArgumentException(INVALID_GROUP_ORDER_TABLE);
        }
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public void addAll(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public List<OrderTable> getValues() {
        return Collections.unmodifiableList(orderTables);
    }
}

package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(TableGroup tableGroup, List<OrderTable> orderTables, int expectedSize) {
        if (expectedSize != orderTables.size()) {
            throw new IllegalArgumentException("올바른 주문 테이블을 입력하세요");
        }
        setTableGroupToOrderTables(tableGroup, orderTables);
        return new OrderTables(orderTables);
    }

    private static void setTableGroupToOrderTables(TableGroup tableGroup, List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(tableGroup);
            orderTable.changeToNotEmpty();
        }
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            validateUngroupOrderStatus(orderTable);
            orderTable.unsetTableGroup();
        }
        orderTables = new ArrayList<>();
    }

    private void validateUngroupOrderStatus(OrderTable orderTable) {
        if (orderTable.containsStartedOrder()) {
            throw new IllegalArgumentException("주문이 있는 단체 지정은 해제할 수 없습니다");
        }
    }

    public List<OrderTable> values() {
        return Collections.unmodifiableList(orderTables);
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(orderTables);
    }
}

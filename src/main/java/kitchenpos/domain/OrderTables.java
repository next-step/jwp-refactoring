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

    public static OrderTables of(List<OrderTable> orderTables, int expectedSize) {
        if (expectedSize != orderTables.size()) {
            throw new IllegalArgumentException("올바른 주문 테이블을 입력하세요");
        }
        return new OrderTables(orderTables);
    }

    public void setTableGroupToOrderTables(TableGroup tableGroup) {
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
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> values() {
        return Collections.unmodifiableList(orderTables);
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(orderTables);
    }
}

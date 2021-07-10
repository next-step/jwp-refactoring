package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL})
    private List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validOrderTablesSize(orderTables);
        validGroupOrderTableList(orderTables);
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    private void validOrderTablesSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("그룹테이블은 2개 이상이어야 그룹화가 가능합니다.");
        }
    }

    private void validGroupOrderTableList(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.validGroupOrderTable();
        }
    }

    public void unGroup(List<Order> orders) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.validOrderStatusCompletion(orders);
            orderTable.unGroup();
        }
        orderTables = Arrays.asList();
    }
}

package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    protected OrderTables() {
        orderTables = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void checkOrderTables() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("그룹핑할 테이블이 부족합니다.");
        }
        orderTables.forEach(orderTable -> orderTable.checkOrderTable());
    }

    public static OrderTables empty() {
        return new OrderTables();
    }

    public void removeTables() {
        orderTables = new ArrayList<>();
    }

    public void checkOrderStatus() {
        orderTables.forEach(orderTable -> orderTable.checkOrderStatus());
    }

    public void addTable(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}

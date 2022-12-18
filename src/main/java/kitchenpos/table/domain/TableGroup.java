package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.order.domain.Order;
import org.springframework.util.CollectionUtils;


@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Embedded
    private final OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    protected TableGroup(Long id, List<OrderTable> orderTables) {
        validOrderTableSize(orderTables);
        this.id = id;
        this.createdDate = LocalDateTime.now();
        orderTables.forEach(this::addOrderTable);
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        return new TableGroup(null, orderTables);
    }

    private void addOrderTable(OrderTable orderTable) {
        orderTable.validGroupingTableGroup();
        orderTable.setTableGroup(this);
        orderTable.notEmpty();
        orderTables.add(orderTable);
    }

    public void ungroup(List<Order> orders) {
        orders.forEach(Order::validIfNotCompletion);
        orderTables.ungroup();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    private void validOrderTableSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("2개 이상부터 그룹 지정이 가능합니다.");
        }
    }
}

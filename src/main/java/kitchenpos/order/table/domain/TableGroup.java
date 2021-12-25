package kitchenpos.order.table.domain;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.table.domain.OrderTable;
import kitchenpos.utils.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Table(name = "table_group")
@Entity
public class TableGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public static TableGroup create() {
        return new TableGroup();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return super.createdDate;
    }

    public void addOrderTable(OrderTable orderTable) {
        orderTable.validateTableGrouping();
        orderTable.notEmpty(this);
        this.orderTables.add(orderTable);
    }

    public void addAllOrderTables(List<OrderTable> orderTables) {
        for(OrderTable orderTable : orderTables) {
            addOrderTable(orderTable);
        }
    }

    public List<Long> findOrderTableIds() {
        return this.orderTables.find().stream()
                .map(OrderTable::getId)
                .collect(toList());
    }

    public List<OrderTable> findOrderTables() {
        return this.orderTables.find();
    }

    public void unGroup() {
        this.orderTables.unGroup();
    }
}

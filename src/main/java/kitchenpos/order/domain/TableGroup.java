package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_group_id")
    private Long id;
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    private TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = OrderTables.of(orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(Long id, LocalDateTime createdDate, OrderTable... orderTables) {
        return new TableGroup(id, createdDate, Arrays.asList(orderTables));
    }

    public static TableGroup of(Long id, LocalDateTime createdDate) {
        return new TableGroup(id, createdDate, Collections.emptyList());
    }

    public static TableGroup of(OrderTables orderTables) {
        return new TableGroup(null, LocalDateTime.now(), orderTables);
    }

    public static TableGroup of() {
        return new TableGroup(null, LocalDateTime.now(), new ArrayList<>());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getTables();
    }

    public void addOrderTable(OrderTable orderTable) {
        orderTable.setTableGroup(this);
        this.orderTables.addTable(orderTable);
    }

    public void addOrderTable(List<OrderTable> orderTableList) {
        orderTableList.forEach(this::addOrderTable);
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.getTableIds();
    }

    public void throwIfOrderTableSizeWrong(int savedOrderTableSize) {
        if (this.orderTables.size() != savedOrderTableSize) {
            throw new IllegalArgumentException();
        }
    }
}

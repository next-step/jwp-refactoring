package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

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
    private Long id;
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    private TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        validateOrderTablesForTableGroup(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = OrderTables.of(orderTables);
    }

    private void validateOrderTablesForTableGroup(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public static TableGroup of(Long id, LocalDateTime createdDate, OrderTable... orderTables) {
        return new TableGroup(id, createdDate, Arrays.asList(orderTables));
    }

    public static TableGroup of(Long id, LocalDateTime createdDate) {
        return new TableGroup(id, createdDate, Collections.emptyList());
    }

    public static TableGroup of() {
        return new TableGroup(null, LocalDateTime.now(), new ArrayList<>());
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(null, LocalDateTime.now(), orderTables);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getTables();
    }

    public void addOrderTable(OrderTable orderTable) {
        orderTable.assignTableGroup(this);
        this.orderTables.addTable(orderTable);
    }

    public void addOrderTable(List<OrderTable> orderTableList) {
        orderTableList.forEach(this::addOrderTable);
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.getTableIds();
    }


    public void group() {
        this.orderTables.group(this);
    }
}

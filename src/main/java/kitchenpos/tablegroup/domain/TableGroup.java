package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.ordertable.domain.OrderTable;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables= new ArrayList<>();
    public static final int MIN_ORDER_TABLE_NUMBER = 2;

    protected TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    private TableGroup(List<OrderTable> orderTables) {
        assignOrderTables(orderTables);
        createdDate = LocalDateTime.now();
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    private void assignOrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
        orderTables.forEach(orderTable -> orderTable.groupBy(this));
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesDuplicated(orderTables);
        validateOrderTablesNotGrouped(orderTables);
        validateOrderTablesEmpty(orderTables);
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (orderTables == null || orderTables.size() < MIN_ORDER_TABLE_NUMBER) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesDuplicated(List<OrderTable> orderTables) {
        if(orderTables.size() != orderTables.stream().distinct().count()){
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesNotGrouped(List<OrderTable> orderTables) {
        if (orderTables.stream().
            anyMatch(orderTable -> orderTable.isGrouped())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream().
            anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

}

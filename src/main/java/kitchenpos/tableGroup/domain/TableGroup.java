package kitchenpos.tableGroup.domain;

import kitchenpos.orderTable.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        assignOrderTables(orderTables);
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        assignOrderTables(orderTables);
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        return new TableGroup(LocalDateTime.now(), orderTables);
    }

    private void assignOrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
        orderTables.forEach(orderTable -> {
                    orderTable.setTableGroup(this);
                    orderTable.setEmpty(false);
                }
        );
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (orderTables == null || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
        if(orderTables.size() != orderTables.stream().distinct().count()){
            throw new IllegalArgumentException();
        }
        if (orderTables.stream().
                anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalArgumentException();
        }
        if (orderTables.stream().
                anyMatch(orderTable -> orderTable.isGrouped())) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}

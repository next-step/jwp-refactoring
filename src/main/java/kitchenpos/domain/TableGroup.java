package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    private static final int MIN_TABLE_COUNT = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        for (OrderTable orderTable : orderTables) {
            add(orderTable);
        }
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLE_COUNT) {
            throw new IllegalArgumentException();
        }
        if (isNotEmptyOrAlreadyGrouped(orderTables)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isNotEmptyOrAlreadyGrouped(List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup()));
    }

    private void add(OrderTable orderTable) {
        if (!this.orderTables.contains(orderTable)) {
            this.orderTables.add(orderTable);
        }
        orderTable.grouping(this);
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.unGrouping();
            orderTables.clear();
        }
    }
}

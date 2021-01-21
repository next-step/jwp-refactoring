package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private final List<OrderTable> orderTables = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void addOrderTables(final OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this(null, createdDate);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroup createTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(createdDate);
        if(validOrderTables(orderTables)) {
            throw new IllegalArgumentException();
        }

        tableGroup.orderTables.addAll(orderTables);
        return tableGroup;
    }

    private static boolean validOrderTables(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup()));
    }
}

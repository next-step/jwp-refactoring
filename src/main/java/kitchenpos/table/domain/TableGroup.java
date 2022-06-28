package kitchenpos.table.domain;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;

@Entity
public class TableGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = ALL)
    private List<OrderTable> orderTables;

    public TableGroup() {
        this(null, new ArrayList<>());
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
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

    public void changeOrderTables(List<OrderTable> orderTables, boolean empty) {
        orderTables.forEach(savedOrderTable -> {
            savedOrderTable.changeTableGroupIdAndEmpty(this);
            savedOrderTable.changeEmpty(empty);
        });
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void validateEmptyAndTableGroups() {
        orderTables.forEach(OrderTable::validateEmptyAndTableGroup);
    }

}

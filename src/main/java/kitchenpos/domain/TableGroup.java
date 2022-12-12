package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    private TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.setEmpty(false);
            savedOrderTable.changeTableGroup(this);
        }
    }

    public TableGroup() {
    }

    public static TableGroup of(Long id, List<OrderTable> requestOrderTables, List<OrderTable> savedOrderTables) {
        validateOrderTables(requestOrderTables, savedOrderTables);

        return new TableGroup(id, LocalDateTime.now(), savedOrderTables);
    }

    private static void validateOrderTables(final List<OrderTable> requestOrderTables, final List<OrderTable> savedOrderTables) {
        validateOrderTable(requestOrderTables, savedOrderTables);
        validateIsCreatableTableGroup(requestOrderTables, savedOrderTables);
    }

    private static void validateOrderTable(final List<OrderTable> requestOrderTables, final List<OrderTable> savedOrderTables) {
        if(requestOrderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateIsCreatableTableGroup(final List<OrderTable> requestOrderTables, final List<OrderTable> savedOrderTables) {
        if (CollectionUtils.isEmpty(requestOrderTables) || requestOrderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
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
}

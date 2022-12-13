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

    private TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.changeEmpty(false);
            savedOrderTable.changeTableGroup(this);
        }
    }

    public TableGroup() {
    }

    public static TableGroup of(List<OrderTable> savedOrderTables) {
        validateOrderTables(savedOrderTables);

        return new TableGroup(LocalDateTime.now(), savedOrderTables);
    }

    private static void validateOrderTables(final List<OrderTable> savedOrderTables) {
        validateIsCreatableTableGroup(savedOrderTables);
    }

    private static void validateIsCreatableTableGroup(final List<OrderTable> savedOrderTables) {
        if (CollectionUtils.isEmpty(savedOrderTables) || savedOrderTables.size() < 2) {
            throw new IllegalArgumentException("그룹을 지정하기 위해서는 주문테이블이 2개 이상 필요합니다.");
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException("그룹을 지정하기 위해서는 테이블은 그룹이 지정되어 있거나 비어있으면 안됩니다.");
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

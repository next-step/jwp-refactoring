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
        validateOrderTables(orderTables);
        this.createdDate = createdDate;
        this.orderTables = orderTables;
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.group(this);
        }
    }

    public TableGroup() {
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(LocalDateTime.now(), orderTables);
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        validateIsCreatableTableGroup(orderTables);
    }

    private void validateIsCreatableTableGroup(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("그룹을 지정하기 위해서는 주문테이블이 2개 이상 필요합니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException("그룹을 지정하기 위해서는 테이블은 그룹이 지정되어 있거나 테이블이 비어있어야 합니다.");
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

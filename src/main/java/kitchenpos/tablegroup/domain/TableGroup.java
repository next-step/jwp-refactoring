package kitchenpos.tablegroup.domain;

import kitchenpos.order.domain.OrderValidatorImpl;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.exception.IllegalOrderTablesException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class TableGroup {
    public static final int ORDERTABLE_MIN = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroupId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
        validateOrderTables();
        groupOrderTables(orderTables);
    }

    private void groupOrderTables(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> orderTable.changeTableGroup(this.id));
    }

    private void validateOrderTables() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < ORDERTABLE_MIN) { //
            throw new IllegalOrderTablesException();
        }
        for (final OrderTable savedOrderTable : orderTables) {
            validateOrderTable(savedOrderTable);
        }
    }

    private void validateOrderTable(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalOrderTablesException();
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

    public void ungroup(OrderValidatorImpl orderValidator) {
        List<Long> orderTableIds = this.orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
        orderValidator.canUngroupOrChangeOrderList(orderTableIds);
        this.orderTables.forEach(orderTable -> orderValidator.canUngroupOrChange(orderTable.getId()));
        this.orderTables.forEach(OrderTable::ungroup);
    }
}

package kitchenpos.table.domain;

import kitchenpos.table.exception.OrderTablesException;
import org.springframework.util.CollectionUtils;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kitchenpos.table.exception.OrderTablesExceptionType.EMPTY_TABLES;
import static kitchenpos.table.exception.OrderTablesExceptionType.LESS_THEN_MIN_TABLE_SIZE;

public class OrderTables {
    private static final int MIN_TABLE_SIZE = 2;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    protected OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }


    public void addOrderTables(Long tableGroupId, List<OrderTable> orderTables) {
        validateTables();
        orderTables.stream().forEach(orderTable -> orderTable.group(tableGroupId));
        orderTables.stream().forEach(orderTable -> this.orderTables.add(orderTable));
    }

    private void validateTables() {
        validateTableSize();
        validateTableStatus();
    }

    private void validateTableSize() {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new OrderTablesException(EMPTY_TABLES);
        }
        if (orderTables.size() < MIN_TABLE_SIZE) {
            throw new OrderTablesException(LESS_THEN_MIN_TABLE_SIZE);
        }
    }

    private void validateTableStatus() {
        boolean isUse = orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId()));
        if (isUse) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup() {
        orderTables.stream().forEach(orderTable -> orderTable.ungroup());
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
    }

    public void addTableGroup(TableGroup tableGroup) {
        validateTables();
        orderTables.stream().forEach(orderTable -> orderTable.group(tableGroup.getId()));
    }
}

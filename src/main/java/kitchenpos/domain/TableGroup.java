package kitchenpos.domain;

import static kitchenpos.exception.CannotCreateGroupTableException.TYPE.HAS_GROUP_TABLE;
import static kitchenpos.exception.CannotCreateGroupTableException.TYPE.HAS_NO_ORDER_TABLE;
import static kitchenpos.exception.CannotCreateGroupTableException.TYPE.INVALID_TABLE_COUNT;
import static kitchenpos.exception.CannotCreateGroupTableException.TYPE.NOT_EMPTY_ORDER_TABLE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

import kitchenpos.exception.CannotCreateGroupTableException;

@Entity
@Table(name = "table_group")
public class TableGroup {

    public static final int MINIMUM_ORDER_TABLE_COUNT = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this.createdDate = LocalDateTime.now();
        addOrderTables(orderTables);
    }

    public static TableGroup create(List<OrderTable> orderTable) {
        validate(orderTable);
        return new TableGroup(orderTable);
    }

    public Long getId() {
        return id;
    }

    private void addOrderTables(List<OrderTable> orderTables) {
        orderTables.forEach(this::addOrderTable);
    }

    private void addOrderTable(OrderTable orderTable) {
        if (!orderTables.contains(orderTable)) {
            orderTables.add(orderTable);
        }
        orderTable.setTableGroup(this);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public static void validate(List<OrderTable> orderTables) {
        if (hasNoOrderTables(orderTables)) {
            throw new CannotCreateGroupTableException(HAS_NO_ORDER_TABLE);
        }
        if (isOrderTablesValidSize(orderTables)) {
            throw new CannotCreateGroupTableException(INVALID_TABLE_COUNT);
        }
        if (isOrderTableNotEmpty(orderTables)) {
            throw new CannotCreateGroupTableException(NOT_EMPTY_ORDER_TABLE);
        }
        if (hasOrderTableGroup(orderTables)) {
            throw new CannotCreateGroupTableException(HAS_GROUP_TABLE);
        }
    }

    private static boolean hasOrderTableGroup(List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(OrderTable::hasTableGroup);
    }

    private static boolean isOrderTableNotEmpty(List<OrderTable> orderTables) {
        return orderTables.stream().noneMatch(OrderTable::isEmpty);
    }

    private static boolean isOrderTablesValidSize(List<OrderTable> orderTables) {
        return orderTables.size() < MINIMUM_ORDER_TABLE_COUNT;
    }

    private static boolean hasNoOrderTables(List<OrderTable> orderTables) {
        return CollectionUtils.isEmpty(orderTables);
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::detachTableGroup);
    }
}

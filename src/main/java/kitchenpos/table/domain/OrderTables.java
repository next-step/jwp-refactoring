package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private List<OrderTable> orderTables = new ArrayList<>();
    private int MIN_TABLE_SIZE = 2;

    public static OrderTables of(List<OrderTable> savedOrderTables) {
        return new OrderTables(savedOrderTables);
    }

    private OrderTables(List<OrderTable> orderTables) {
        if (isInvalidTableSize(orderTables)) {
            throw new IllegalArgumentException("");
        }
        if (isInvalidNotCreated(orderTables)) {
            throw new IllegalArgumentException();
        }
        this.orderTables.addAll(orderTables);
    }

    protected OrderTables() {
    }

    ;

    private boolean isInvalidTableSize(final List<OrderTable> orderTables) {
        return CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLE_SIZE;
    }

    private boolean isInvalidNotCreated(final List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(getOrderTablePredicate());
    }

    private Predicate<OrderTable> getOrderTablePredicate() {
        return orderTable -> !orderTable.getOrderTableStatus().isEmpty() || Objects.nonNull(
            orderTable.getTableGroup());
    }

    public void unGroup() {
        orderTables.stream()
            .forEach(OrderTable::unGroup);
    }

    public List<OrderTable> getList() {
        return Collections.unmodifiableList(orderTables);
    }

    public void addAll(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public int size() {
        return orderTables.size();
    }

    public List<Long> getIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }
}

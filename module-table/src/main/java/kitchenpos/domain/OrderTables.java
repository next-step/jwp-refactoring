package kitchenpos.domain;

import kitchenpos.exception.TableExceptionType;
import kitchenpos.exception.TableException;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    private static final int GROUP_MIN_COUNT = 2;

    @OneToMany(mappedBy = "tableGroupId")
    @Column(name = "orderTables")
    private final List<OrderTable> collections;

    protected OrderTables() {
        collections = new ArrayList<>();
    }

    public OrderTables(final List<OrderTable> collections) {
        validate(collections);
        this.collections = Collections.unmodifiableList(collections);
    }

    private void validate(final List<OrderTable> collections) {
        if (CollectionUtils.isEmpty(collections) || collections.size() < GROUP_MIN_COUNT) {
            throw new TableException(TableExceptionType.LEAK_TABLE_COUNT);
        }

        if (usedTable(collections)) {
            throw new TableException(TableExceptionType.USED_TABLE);
        }
    }

    private boolean usedTable(List<OrderTable> collections) {
        return collections.stream()
                .anyMatch(it -> !it.isEmpty());
    }

    public static OrderTables of(final List<OrderTable> collections) {
        return new OrderTables(collections);
    }

    public void notMatchCount(final int requestOrderTableSize) {
        if (collections.size() != requestOrderTableSize) {
            throw new TableException(TableExceptionType.DIFFER_TABLE_COUNT);
        }
    }

    public void updateGroupTableIdAndEmpty(final Long groupId, final boolean empty) {
        for (OrderTable orderTable : collections) {
            orderTable.updateTableGroupId(groupId);
            orderTable.updateEmpty(empty);
        }
    }

    public List<Long> getOrderTableIds() {
        return Collections.unmodifiableList(collections.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList()));
    }

    public List<OrderTable> get() {
        return Collections.unmodifiableList(collections);
    }

    @Override
    public String toString() {
        return "OrderTables{" +
                "collections=" + collections +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTables that = (OrderTables) o;
        return Objects.equals(collections, that.collections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collections);
    }
}

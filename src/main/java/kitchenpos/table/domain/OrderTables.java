package kitchenpos.table.domain;

import kitchenpos.table.exception.TableException;
import kitchenpos.table.exception.TableExceptionType;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    void notMatchCount(final List<OrderTable> orderTables) {
        if (collections.size() != orderTables.size()) {
            throw new TableException(TableExceptionType.DIFFER_TABLE_COUNT);
        }
    }
    void updateGroupTableIdAndEmpty(final Long groupId) {
        for (OrderTable orderTable : collections) {
            orderTable.updateTableGroupId(groupId);
            orderTable.updateEmpty(false);
        }
    }

    public List<OrderTable> get() {
        return Collections.unmodifiableList(collections);
    }

}

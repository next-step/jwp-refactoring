package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;

@Embeddable
public class OrderTables {
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> items = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> items, TableGroup tableGroup) {
        validateTablesEmpty(items);
        tablesMapIntoGroup(items, tableGroup);
        this.items = items;
    }

    public List<OrderTable> getItems() {
        return items;
    }

    private void validateTablesEmpty(List<OrderTable> items) {
        items.stream()
            .filter(it -> !it.isEmpty() || it.isGrouped())
            .findFirst()
            .ifPresent(e -> {
                throw new CannotCreateException(ExceptionType.MUST_NOT_BE_EMPTY_OR_GROUPED_TABLE);
            });
    }

    private void tablesMapIntoGroup(List<OrderTable> items, TableGroup tableGroup) {
        items.forEach(it -> it.mapIntoGroup(tableGroup));
    }

    public void unGroup() {
        items.forEach(OrderTable::unGroup);
    }
}

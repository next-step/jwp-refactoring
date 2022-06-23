package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> items = new ArrayList<>();

    public void addAll(List<OrderTable> orderTables) {
        this.items.addAll(orderTables);
    }

    public List<OrderTable> getItems() {
        return items;
    }

    public void validateTablesEmpty() {
        this.items.stream()
            .filter(it -> !it.isEmpty() || it.isGrouped())
            .findFirst()
            .ifPresent(e -> {
                throw new IllegalArgumentException();
            });
    }

    public void tablesMapIntoGroup(Long tableGroupId) {
        items.forEach(it -> it.mapIntoGroup(tableGroupId));
    }

    public void unGroup() {
        items.forEach(OrderTable::unGroup);
    }
}

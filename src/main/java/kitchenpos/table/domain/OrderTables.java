package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY)
    private List<OrderTableEntity> items = new ArrayList<>();

    public void addAll(List<OrderTableEntity> orderTables) {
        this.items.addAll(orderTables);
    }

    public List<OrderTableEntity> getItems() {
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
        items.forEach(it -> it.unGroup());
    }
}

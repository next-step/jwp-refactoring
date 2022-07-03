package kitchenpos.table.domain;


import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> items = new ArrayList<>();

    public OrderTables() {
    }

    public void addAll(Long tableGroupId, List<OrderTable> items) {
        this.items = items;
        tablesMapIntoGroup(tableGroupId, items);
    }

    private void tablesMapIntoGroup(Long tableGroupId, List<OrderTable> items) {
        items.forEach(it -> it.mapIntoGroup(tableGroupId));
    }

    public List<OrderTable> getItems() {
        return Collections.unmodifiableList(items);
    }
}

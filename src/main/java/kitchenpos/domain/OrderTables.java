package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.util.Objects.requireNonNull;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTableEntity> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public void addAll(TableGroupEntity tableGroup, List<OrderTableEntity> orderTables) {
        requireNonNull(tableGroup, "tableGroup");
        requireNonNull(orderTables, "orderTables");
        for (OrderTableEntity orderTable : orderTables) {
            add(tableGroup, orderTable);
        }
    }

    private void add(TableGroupEntity tableGroup, OrderTableEntity orderTable) {
        if (!this.orderTables.contains(orderTable)) {
            orderTables.add(orderTable);
        }
        orderTable.bindTo(tableGroup);
    }

    public List<OrderTableEntity> get() {
        return Collections.unmodifiableList(orderTables);
    }
}

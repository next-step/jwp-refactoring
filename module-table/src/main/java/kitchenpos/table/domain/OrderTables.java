package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.MERGE})
    private final List<OrderTable> elements = new ArrayList<>();

    protected OrderTables() {
    }


    void addOrderTables(List<OrderTable> orderTables) {
        elements.addAll(orderTables);
    }


    void includeToTableGroup(TableGroup tableGroup) {
        elements.forEach(orderTable -> orderTable.includeTo(tableGroup));
    }

    void ungroup() {
        elements.forEach(OrderTable::ungroup);
    }

    List<OrderTable> toList() {
        return Collections.unmodifiableList(elements);
    }
}

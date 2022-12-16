package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> values;

    protected OrderTables() {
        values = new ArrayList<>();
    }

    public void add(OrderTable orderTable) {
        values.add(orderTable);
    }

    public List<OrderTable> values() {
        return Collections.unmodifiableList(values);
    }

    public void removeAll() {
        values.clear();
    }
}

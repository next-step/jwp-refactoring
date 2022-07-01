package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    private List<OrderTable> elements = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        elements = new ArrayList<>(orderTables);
    }

    public List<OrderTable> elements() {
        return Collections.unmodifiableList(elements);
    }
}

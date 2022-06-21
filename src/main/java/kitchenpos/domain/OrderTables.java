package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<OrderTable> orderTables = new ArrayList<>();

    public List<OrderTable> getAll() {
        return this.orderTables;
    }

    public void add(OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }
}

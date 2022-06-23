package kitchenpos.table.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Embeddable
public class OrderTables implements Iterable<OrderTable> {
    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables(long size, List<OrderTable> orderTables) {
        if (size != orderTables.size()){
            throw new IllegalArgumentException();
        }
        this.orderTables = orderTables;
    }

    public OrderTables() {
    }

    public List<OrderTable> getAll() {
        return this.orderTables;
    }

    public void add(OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }

    @Override
    public Iterator<OrderTable> iterator() {
        return this.orderTables.iterator();
    }
}

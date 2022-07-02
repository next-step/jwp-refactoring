package kitchenpos.domain.table;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Embeddable
public class OrderTables implements Iterable<OrderTable> {
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    @JoinColumn(name = "tableGroupId")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables(long size, List<OrderTable> orderTables) {
        if (size != orderTables.size()){
            throw new IllegalArgumentException("테이블 갯수가 적절하지 않습니다.");
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

    public long size() {
        return orderTables.size();
    }
}

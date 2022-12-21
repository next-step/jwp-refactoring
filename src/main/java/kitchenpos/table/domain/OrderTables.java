package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroupId", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    public List<OrderTable> getUnmodifiableOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public void add(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public void ungroup() {
        if (orderTables.isEmpty()) {
            throw new IllegalArgumentException("등록된 주문테이블이 없습니다.");
        }
        orderTables.forEach(OrderTable::ungroup);
        orderTables.clear();
    }
}

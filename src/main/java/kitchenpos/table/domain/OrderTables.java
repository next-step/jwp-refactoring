package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderTable> data = new ArrayList<>();

    protected OrderTables() { }

    public void add(OrderTable orderTable) {
        data.add(orderTable);
    }

    public void ungroup() {

        if (CollectionUtils.isEmpty(data)) {
            return;
        }

        if (hasNotCompletedOrder()) {
            throw new IllegalArgumentException();
        }

        data.forEach(OrderTable::ungroup);
        data.clear();
    }

    private boolean hasNotCompletedOrder() {
        return data.stream().anyMatch(orderTable -> !orderTable.isAllOrderCompleted());
    }

    public int size() {
        return data.size();
    }

    public List<OrderTable> getData() {
        return Collections.unmodifiableList(data);
    }
}

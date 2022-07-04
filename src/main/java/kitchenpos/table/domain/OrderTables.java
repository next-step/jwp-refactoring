package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {
    public static final Integer MIN_ORDERTABLES_SIZE = 2;

    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderTable> elements = new ArrayList<>();

    protected OrderTables() {
    }

    public int size() {
        return elements.size();
    }

    public void add(OrderTable orderTable) {
        elements.add(orderTable);
    }

    public void clear() {
        elements.clear();
    }

    public List<Long> getOrderTableIds() {
        return elements.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> get() {
        return new ArrayList<>(elements);
    }
}

package kitchenpos.domain.tablegroup;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.domain.table.OrderTable;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> values;

    protected OrderTables(){}

    private OrderTables(List<OrderTable> orderTables) {
        this.values = orderTables;
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public static OrderTables createEmpty() {
        return new OrderTables(Lists.newArrayList());
    }

    public List<OrderTable> getReadOnlyValues() {
        return Collections.unmodifiableList(this.values);
    }

    public List<Long> getIds() {
        return this.values.stream().map(OrderTable::getId).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTables that = (OrderTables) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}

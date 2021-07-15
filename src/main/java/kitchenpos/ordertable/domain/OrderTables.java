package kitchenpos.ordertable.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public static OrderTables of(OrderTable... orderTables) {
        return new OrderTables(Arrays.asList(orderTables));
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(orderTables);
    }

    public boolean hasLargerOrSameSizeOf(int size) {
        return orderTables.size() >= size;
    }

    public boolean isGroupable() {
        return orderTables.stream().allMatch(OrderTable::isGroupable);
    }
}

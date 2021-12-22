package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    static final int MINIMUM_TABLES = 2;

    @OneToMany(mappedBy = "tableGroup", orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        validateGreaterOrEqualsMinimum(orderTables);

        this.orderTables = orderTables;
    }

    public static OrderTables of(final List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    private void validateGreaterOrEqualsMinimum(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_TABLES) {
            throw new IllegalArgumentException(
                String.format("최소 %d 개 이상의 테이블이 필요합니다.", MINIMUM_TABLES));
        }
    }
}

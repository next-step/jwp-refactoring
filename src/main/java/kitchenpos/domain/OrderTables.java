package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    private static void validCheckOrderTableIsEmptyAndMinSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        validCheckOrderTableIsEmptyAndMinSize(orderTables);
        return new OrderTables(orderTables);
    }

    public void ungroup() {
        validCheckIsNotEmptyAndHasNotGroup();

        orderTables.forEach(OrderTable::ungroup);
    }

    private void validCheckIsNotEmptyAndHasNotGroup() {
        validIsNotEmpty();
        validHasGroup();
    }

    private void validHasGroup() {
        boolean matchGroup = orderTables.stream()
                .anyMatch(OrderTable::isNotNull);

        if (matchGroup) {
            throw new IllegalArgumentException();
        }
    }

    private void validIsNotEmpty() {
        boolean matchIsNotEmpty = orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());

        if (matchIsNotEmpty) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

}

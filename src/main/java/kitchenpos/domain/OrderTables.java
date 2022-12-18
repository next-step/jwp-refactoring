package kitchenpos.domain;

import org.hibernate.engine.internal.Collections;
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

    public OrderTables(List<OrderTable> orderTables) {
        validCheckOrderTableIsEmptyAndMinSize(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    private void validCheckOrderTableIsEmptyAndMinSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }
    private void validHasGroup() {

        System.out.println(orderTables.toString());

        boolean matchGroup = orderTables.stream()
                .anyMatch(OrderTable::isNotNull);

        System.out.println("=-==------");
        System.out.println(matchGroup);

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

    public void validCheckTableGroup() {
        validIsNotEmpty();
        validHasGroup();
    }
}

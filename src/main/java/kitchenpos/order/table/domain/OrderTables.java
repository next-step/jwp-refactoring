package kitchenpos.order.table.domain;

import kitchenpos.order.domain.Order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    private void validateOrderStatusCookingOrMeal() {
        orderTables.stream()
                .map(OrderTable::getOrder)
                .filter(Order::notIsCompletion)
                .findFirst()
                .ifPresent(o -> { throw new IllegalArgumentException("완료되지 않은 주문이 존재합니다.");});
    }

    public void add(OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }

    public List<OrderTable> find() {
        return this.orderTables;
    }

    public void unGroup() {

        validateOrderStatusCookingOrMeal();

        for (final OrderTable orderTable : this.orderTables) {
            orderTable.unGroup();
        }

        this.orderTables.clear();
    }
}

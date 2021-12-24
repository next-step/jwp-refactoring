package kitchenpos.order.domain;

import kitchenpos.order.application.exception.InvalidOrderState;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = {PERSIST, MERGE}, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public void validateTableState() {
        boolean isAllCompleted = orderTables.stream()
                .allMatch(OrderTable::isCompleted);

        if (!isAllCompleted) {
            throw new InvalidOrderState("모든 주문 상태가 완료되지 않아 단체석을 해제할 수 없습니다.");
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}

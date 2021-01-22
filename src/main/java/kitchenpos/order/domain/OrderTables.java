package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    private static final int MIN_TABLE_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        checkOrderTableSize(orderTables);
        for(OrderTable orderTable : orderTables) {
            orderTable.isOrderTableEmptyOrAssigned();
        }
    }

    public List<OrderTable> findAll() {
        return Collections.unmodifiableList(this.orderTables);
    }

    public boolean isSameSize(int otherSize) {
        return this.orderTables.size() == otherSize;
    }

    public void updateTableGroup(TableGroup tableGroup) {
        orderTables.forEach(table -> table.updateTableGroup(tableGroup));
    }

    public void unGroup() {
        this.orderTables.forEach(OrderTable::unGroup);
    }

    public void checkOrderTableStatus() {
        boolean hasNotCompleteOrder = orderTables.stream()
                .anyMatch(OrderTable::isNotComplete);
        if (hasNotCompleteOrder) {
            throw new IllegalArgumentException("주문 상태가 조리중이거나 식사중인 테이블의 그룹 지정은 해지할 수 없습니다.");
        }
    }

    private void checkOrderTableSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLE_SIZE) {
            throw new IllegalArgumentException("2개 미만의 테이블은 그룹 지정을 할 수 없습니다.");
        }
    }
}

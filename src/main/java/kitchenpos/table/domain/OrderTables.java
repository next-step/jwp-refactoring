package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private final List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {

    }

    public OrderTables(List<OrderTable> orderTables) {
        addOrderTables(orderTables);
    }

    public void validateForCreateTableGroup(OrderTables orderTables) {
        validateMinimumCount();
        validateSameSize(orderTables.getOrderTables().size());

        for (final OrderTable orderTable : orderTables.getOrderTables()) {
            if (orderTable.isNotEmptyAndNotExistTableGroup()) {
                throw new IllegalArgumentException("빈 테이블이 아니거나 이미 단체 지정된 테이블 입니다.");
            }
        }
    }

    public List<Long> findOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void updateEmpty(boolean empty) {
        orderTables.forEach(orderTable -> orderTable.updateEmpty(empty));
    }

    public void addOrderTables(OrderTables orderTables) {
        addOrderTables(orderTables.getOrderTables());
    }

    private void addOrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    private void validateMinimumCount() {
        if (CollectionUtils.isEmpty(this.orderTables) || this.orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블의 수가 2개 이상이어야 합니다.");
        }
    }

    private void validateSameSize(int size) {
        if (orderTables.size() != size) {
            throw new IllegalArgumentException("유효하지 않은 주문 테이블이 존재 합니다.");
        }
    }

    public void updateTableGroup(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(tableGroup));
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}

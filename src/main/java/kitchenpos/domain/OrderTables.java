package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public boolean sameSizeAs(int size) {
        return orderTables.size() == size;
    }

    private void validate(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void validateTableGroupCreatable() {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty()) {
                throw new IllegalArgumentException("빈테이블은 단체 지정할 수 없습니다.");
            }
            if (Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException("이미 단체가 있으면 단체 지정할 수 없습니다.");
            }
        }
    }

    private void validateUngroupPossible() {
        if (!isUngroupPossible()) {
            throw new IllegalArgumentException("MEAL 이나 COOKING 상태인 주문이 있으면 단체 해제할 수 없습니다.");
        }
    }

    private boolean isUngroupPossible() {
        return orderTables.stream()
            .noneMatch(orderTable ->
                orderTable.hasOrderWithStatus(OrderStatus.MEAL)
                    || orderTable.hasOrderWithStatus(OrderStatus.COOKING));
    }

    public void toGroup(TableGroup tableGroup) {
        validateTableGroupCreatable();
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.toGroup(tableGroup);
        }
        tableGroup.setOrderTables(this);
    }

    public void ungroup() {
        validateUngroupPossible();
        for (final OrderTable orderTable : orderTables) {
            orderTable.toGroup(null);
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }
}

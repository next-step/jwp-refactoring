package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderTables {

    private static final int MIN_GROUP_SIZE = 2;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables, final TableGroup tableGroup) {
        validate(orderTables);
        assign(orderTables, tableGroup);
        this.orderTables.addAll(orderTables);
    }

    private void validate(final List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_GROUP_SIZE) {
            throw new IllegalArgumentException(String.format("최소 %d개 이상 단체 지정 할 수 있습니다.", MIN_GROUP_SIZE));
        }

        if (hasNotEmptyTable(orderTables)) {
            throw new IllegalArgumentException("주문 테이블이 등록 가능 상태면 단체 지정할 수 없습니다.");
        }

        if (isAssigned(orderTables)) {
            throw new IllegalArgumentException("이미 단체 지정이 되어 있으면 지정할 수 없다.");
        }
    }

    private boolean hasNotEmptyTable(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());
    }

    private boolean isAssigned(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> Objects.nonNull(orderTable.getTableGroupId()));
    }

    private void assign(final List<OrderTable> orderTables, final TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.assign(tableGroup.getId()));
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}

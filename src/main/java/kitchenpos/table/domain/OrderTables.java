package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public static OrderTables empty() {
        return new OrderTables();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void add(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public List<Long> getIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void ungroup() {
        orderTables.forEach(orderTable -> orderTable.setTableGroup(null));
    }

    public void validateGroup(List<Long> orderTableIds) {
        validateGroupTable();
        validateSize(orderTableIds);
    }

    private void validateGroupTable() {
        for (final OrderTable orderTable : orderTables) {
            if (orderTable.getEmpty().isNotEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException("테이블이 비어있지 않거나, 테이블 그룹이 존재하지 않습니다.");
            }
        }
    }

    private void validateSize(List<Long> orderTableIds) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("테이블을 그룹화할 주문 테이블 수가 일치하지 않습니다.");
        }
    }
}

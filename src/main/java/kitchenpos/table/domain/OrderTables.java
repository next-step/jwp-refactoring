package kitchenpos.table.domain;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private List<OrderTable> orderTables;

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<Long> extractIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(toList());
    }

    public void groupBy(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.groupBy(tableGroup);
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    public void validateForCreatableGroup() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블이 두 개 이상 있어야 합니다");
        }
    }

    public void validateForCreatableGroup(OrderTables orderTables) {
        validateForEqualsSize(orderTables);
        validateForGroupable();
    }

    private void validateForGroupable() {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isGroupable()) {
                throw new IllegalArgumentException("테이블이 그룹할 수 없는 상태입니다");
            }
        }
    }

    private void validateForEqualsSize(OrderTables other) {
        if (this.orderTables.size() != other.orderTables.size()) {
            throw new IllegalArgumentException("요청한 테이블이 없습니다");
        }
    }

    public List<OrderTable> getValues() {
        return orderTables;
    }
}

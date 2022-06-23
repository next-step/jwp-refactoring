package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


public class OrderTables {

    private List<OrderTable> orderTables;

    private OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> getValues() {
        return orderTables;
    }

    public List<Long> getIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void validate(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블이 두 개 이상 있어야 합니다");
        }
    }
}
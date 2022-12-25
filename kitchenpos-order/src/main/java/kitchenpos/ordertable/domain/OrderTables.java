package kitchenpos.ordertable.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.OneToMany;
import kitchenpos.common.error.ErrorEnum;
import org.springframework.util.CollectionUtils;

public class OrderTables {
    private static final int MINIMUM_ORDER_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {}

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    private void validate(List<OrderTable> orderTables) {
        validateIsEmpty(orderTables);
        validateMinimumSize(orderTables);
    }

    private void validateIsEmpty(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException(ErrorEnum.NOT_EXISTS_ORDER_TABLE_LIST.message());
        }
    }

    private void validateMinimumSize(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_ORDER_SIZE) {
            throw new IllegalArgumentException(ErrorEnum.ORDER_TABLE_TWO_OVER.message());
        }
    }

    public List<OrderTable> get() {
        return Collections.unmodifiableList(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }
    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void group(Long tableGroupId) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException(ErrorEnum.NOT_EXISTS_ORDER_TABLE_LIST.message());
        }
        if (orderTables.size() < MINIMUM_ORDER_SIZE) {
            throw new IllegalArgumentException(ErrorEnum.GUESTS_UNDER_ZERO.message());
        }
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(tableGroupId));
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }
}

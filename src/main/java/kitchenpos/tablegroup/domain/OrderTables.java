package kitchenpos.tablegroup.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;
import kitchenpos.ordertable.domain.OrderTable;

@Embeddable
public class OrderTables {
    public OrderTables() {}

    public static final int MIN_SIZE = 2;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        checkValid(orderTables);
        checkOrderTableIsEmpty(orderTables);
        return new OrderTables(orderTables);
    }

    private static void checkValid(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_SIZE) {
            throw new CustomException(ErrorInfo.INVALID_REQUEST_ORDER_TABLE_SIZE);
        }
    }

    private static void checkOrderTableIsEmpty(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> {
            if (!orderTable.isEmpty()) {
                throw new CustomException(ErrorInfo.ORDER_TABLE_IS_NOT_EMPTY);
            }
        });
    }

    public void init(TableGroup tableGroup) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(tableGroup);
            orderTable.empty(false);
        }
    }

    public int size() {
        return orderTables.size();
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void unGroup() {
        orderTables.forEach(orderTable -> orderTable.setTableGroup(null));
    }
}

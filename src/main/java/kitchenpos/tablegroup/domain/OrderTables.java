package kitchenpos.tablegroup.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import kitchenpos.common.error.InvalidRequestException;
import kitchenpos.common.error.OrderTableNotEmptyException;
import kitchenpos.order.domain.OrderTable;

@Embeddable
public class OrderTables {
    public OrderTables() {}

    public static final int MIN_SIZE = 2;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(TableGroup tableGroup, List<OrderTable> orderTableList) {
        checkValid(orderTableList);
        checkOrderTableIsEmpty(orderTableList);
        for (OrderTable orderTable : orderTableList) {
            orderTable.setTableGroup(tableGroup);
        }
        return new OrderTables(orderTableList);
    }

    private static void checkValid(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_SIZE) {
            throw new InvalidRequestException();
        }
    }

    private static void checkOrderTableIsEmpty(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> {
            if (!orderTable.isEmpty()) {
                throw new OrderTableNotEmptyException();
            }
        });
    }

    public int size() {
        return orderTables.size();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setTableGroup(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(tableGroup);
        }
    }

    public List<Long> orderIds() {
        return orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }
}

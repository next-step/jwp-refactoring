package kitchenpos.ordertable.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.exception.GroupTablesException;
import kitchenpos.ordertable.exception.UngroupTablesException;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    private static final String ERROR_MESSAGE_NOT_ENOUGH_TABLES = "주문 테이블이 2개 이상일때 그룹화 가능 합니다.";
    private static final String ERROR_MESSAGE_NOT_EMPTY_TABLE = "그룹화를 위해선 테이블들이 주문종료 상태여야 합니다.";
    private static final String ERROR_MESSAGE_TABLE_ALREADY_IN_GROUP = "테이블 그룹에 이미 속해있는 주문테이블은 그룹화할 수 없습니다..";
    private static final String ERROR_MESSAGE_DUPLICATE_TALBES = "그룹대상 테이블에 중복이 존재합니다.";
    private static final String ERROR_MESSAGE_ORDER_NOT_COMPLETE = "조리, 식사중인 주문이 존재하는 테이블이 있습니다.";

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.MERGE)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected void groupTables(List<OrderTable> inputOrderTables, TableGroup tableGroup) {
        validateGroupingCondition(inputOrderTables);
        for (OrderTable orderTable : inputOrderTables) {
            orderTable.groupIn(tableGroup);
            orderTable.changeOrderClose(false);
        }
        orderTables.addAll(inputOrderTables);
    }

    private void validateGroupingCondition(List<OrderTable> inputOrderTables) {
        validateAtLeastTwoDistinctTables(inputOrderTables);
        inputOrderTables.stream()
            .forEach(orderTable -> validateOrderTableStatus(orderTable));
    }

    private void validateAtLeastTwoDistinctTables(List<OrderTable> inputOrderTables) {
        validateNoDuplicateTable(inputOrderTables);

        if (CollectionUtils.isEmpty(inputOrderTables) || inputOrderTables.size() < 2) {
            throw new GroupTablesException(ERROR_MESSAGE_NOT_ENOUGH_TABLES);
        }
    }

    private void validateOrderTableStatus(OrderTable orderTable) {
        validateEmptyTable(orderTable);
        validateNotInAnyGroup(orderTable);
    }

    private void validateNoDuplicateTable(List<OrderTable> inputOrderTables) {
        Set<OrderTable> distinctOrderTables = inputOrderTables.stream().collect(Collectors.toSet());

        if (inputOrderTables.size() != distinctOrderTables.size()) {
            throw new GroupTablesException(ERROR_MESSAGE_DUPLICATE_TALBES);
        }
    }

    private void validateNotInAnyGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new GroupTablesException(ERROR_MESSAGE_TABLE_ALREADY_IN_GROUP);
        }
    }

    private void validateEmptyTable(OrderTable orderTable) {
        if (!orderTable.isOrderClose()) {
            throw new GroupTablesException(ERROR_MESSAGE_NOT_EMPTY_TABLE);
        }
    }

    protected void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            validateAllOrdersComplete(orderTable.getOrders());
            orderTable.unGroup();
        }
        orderTables.clear();
    }

    private void validateAllOrdersComplete(List<Order> orders) {
        long completeCount = orders.stream()
            .filter(Order::isCompleteStatus)
            .count();
        if (orders.size() != completeCount) {
            throw new UngroupTablesException(ERROR_MESSAGE_ORDER_NOT_COMPLETE);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public int getSize() {
        return orderTables.size();
    }
}

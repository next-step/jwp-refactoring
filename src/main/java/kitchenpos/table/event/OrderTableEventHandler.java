package kitchenpos.table.event;

import kitchenpos.exception.OrderTableException;
import kitchenpos.exception.TableGroupException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class OrderTableEventHandler {

    public static final String NOT_CHANGE_GROUP_TABLE_ERROR_MESSAGE = "그룹핑 되어있는 테이블 상태를 변경할 수 없습니다.";
    public static final String MINIMUM_GROUP_TABLE_COUNT_ERROR_MESSAGE = "2개 이상의 테이블을 그룹핑할 수 있습니다.";
    public static final String CONTAIN_ORDER_STATUS_COMPLETION_ERROR_MESSAGE = "주문 상태가 완료 상태가 아닌 주문 테이블이 존재하여 그룹 해제에 실패하였습니다.";
    public static final String ORDER_STATUS_COMPLETION_ERROR_MESSAGE = "주문 상태가 완료 상태가 아닌 경우 테이블 상태를 변경할 수 없습니다.";

    private final OrderRepository orderRepository;

    public OrderTableEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void groupOrderTable(OrderTableGroupEvent orderTableGroupEvent) {
        validateTableIds(orderTableGroupEvent.getOrderTableIds());
        validateOrderTables(orderTableGroupEvent.getOrderTables(), orderTableGroupEvent.getOrderTableIds());
    }

    @EventListener
    public void ungroupOrderTable(OrderTableUngroupEvent orderTableUngroupEvent) {
        TableGroup tableGroup = orderTableUngroupEvent.getTableGroup();
        OrderTables orderTables = tableGroup.getOrderTables();
        validateUngroup(orderTables.generateOrderTableIds());
        tableGroup.updateUnGroup();
    }

    @EventListener
    public void changeEmptyOrderTable(OrderTableChangeEmptyValidEvent orderTableChangeEmptyValidEvent) {
        OrderTable orderTable = orderTableChangeEmptyValidEvent.getOrderTable();
        validateIsGroupTable(orderTable);
        Optional<Orders> optionalOrder = orderRepository.findByOrderTableId(orderTable.getId());
        optionalOrder.ifPresent(order -> validateOrderStatus(order));
    }

    private void validateTableIds(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new TableGroupException(MINIMUM_GROUP_TABLE_COUNT_ERROR_MESSAGE);
        }
    }

    private void validateOrderTables(OrderTables orderTables, List<Long> orderTableIds) {
        orderTables.checkValidEqualToRequestSize(orderTableIds);
        orderTables.checkValidEmptyTableGroup();
    }

    private void validateUngroup(List<Long> orderTableIds) {
        List<Orders> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        if (orders.stream().anyMatch(order -> !order.isCompletion())) {
            throw new TableGroupException(CONTAIN_ORDER_STATUS_COMPLETION_ERROR_MESSAGE);
        }
    }

    private void validateIsGroupTable(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new OrderTableException(NOT_CHANGE_GROUP_TABLE_ERROR_MESSAGE);
        }
    }

    private void validateOrderStatus(Orders order) {
        if (!order.isCompletion()) {
            throw new OrderTableException(ORDER_STATUS_COMPLETION_ERROR_MESSAGE);
        }
    }
}

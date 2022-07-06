package kitchenpos.tablegroup.validator;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kitchenpos.common.Messages.*;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreateRequestTableGroup(TableGroupRequest tableGroupRequest) {
        validateOrderTableIds(tableGroupRequest.getOrderTableIds());
    }

    private void validateOrderTableIds(List<OrderTableIdRequest> orderTablesIds) {
        if (CollectionUtils.isEmpty(orderTablesIds) || orderTablesIds.size() < 2) {
            throw new IllegalArgumentException(TABLE_GROUP_ORDER_IDS_REQUIRED);
        }
    }

    public void validateCreateOrderTableGroup(List<Long> requestOrderTablesIds) {
        List<OrderTable> orderTables = orderTableRepository.findByIdIn(requestOrderTablesIds);

        if (requestOrderTablesIds.size() != orderTables.size()) {
            throw new IllegalArgumentException(TABLE_GROUP_ORDER_IDS_FIND_IN_NO_SUCH);
        }

        for (final OrderTable orderTable : orderTables) {
            if (orderTable.isNotEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException(TABLE_GROUP_ORDER_NOT_EMPTY);
            }
        }
    }

    public void validateUnGroup(TableGroup tableGroup) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        if (isOrderTablesStateInCookingOrMeal(orderTables)) {
            throw new IllegalArgumentException(ORDER_TABLE_STATUS_CANNOT_UPDATE);
        }
    }

    private boolean isOrderTablesStateInCookingOrMeal(List<OrderTable> orderTables) {
        List<Long> orderTablesIds = orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTablesIds, orderStatuses);
    }
}

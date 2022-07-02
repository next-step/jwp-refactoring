package kitchenpos.tablegroup.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static kitchenpos.common.Messages.ORDER_TABLE_STATUS_CANNOT_UPDATE;
import static kitchenpos.common.Messages.TABLE_GROUP_ORDER_IDS_REQUIRED;

@Service
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;
    private final OrderService orderService;

    public TableGroupService(TableService tableService, TableGroupRepository tableGroupRepository, OrderService orderService) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
        this.orderService = orderService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> requestOrderTablesIds = convertOrderTableIds(tableGroupRequest.getOrderTableIds());
        validateOrderTableIds(requestOrderTablesIds);

        List<OrderTable> tables = tableService.findByIdIn(requestOrderTablesIds);
        OrderTables orderTables = OrderTables.of(tables);
        orderTables.validateOrderTableGroup(requestOrderTablesIds);

        TableGroup tableGroup = TableGroup.of(orderTables);
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    private List<Long> convertOrderTableIds(List<OrderTableIdRequest> OrderTableIdRequests) {
        return OrderTableIdRequests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    private void validateOrderTableIds(List<Long> orderTablesIds) {
        if (CollectionUtils.isEmpty(orderTablesIds) || orderTablesIds.size() < 2) {
            throw new IllegalArgumentException(TABLE_GROUP_ORDER_IDS_REQUIRED);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(NoSuchElementException::new);

        OrderTables orderTables = OrderTables.of(tableGroup.getOrderTables());
        if (orderService.isOrderTablesStateInCookingOrMeal(orderTables.getOrderTables())) {
            throw new IllegalArgumentException(ORDER_TABLE_STATUS_CANNOT_UPDATE);
        }

        orderTables.ungroup();
    }
}

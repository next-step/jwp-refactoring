package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.event.GroupedTablesEvent;
import kitchenpos.common.event.UngroupedTablesEvent;
import kitchenpos.order.application.OrderTableGroupService;
import kitchenpos.order.domain.Order;
import kitchenpos.table.application.TableTableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@Service
@Transactional
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableTableGroupService tableTableGroupService;
    private final OrderTableGroupService orderTableGroupService;
    private final TableGroupValidator tableGroupValidator;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(final ApplicationEventPublisher publisher, final TableGroupRepository tableGroupRepository,
                             final TableTableGroupService tableTableGroupService, final OrderTableGroupService orderTableGroupService,
                             final TableGroupValidator tableGroupValidator) {
        this.publisher = publisher;
        this.tableGroupRepository = tableGroupRepository;
        this.tableTableGroupService = tableTableGroupService;
        this.orderTableGroupService = orderTableGroupService;
        this.tableGroupValidator = tableGroupValidator;
    }

    public void ungroup(final Long tableGroupId) {
        List<Long> orderTableIds = tableTableGroupService.findOrderTableIdsByTableGroupId(tableGroupId);
        List<Order> orders = orderTableGroupService.findOrdersByOrderTableIdIn(orderTableIds);
        tableGroupValidator.validateExistsOrdersStatusIsCookingOrMeal(orders);
        publisher.publishEvent(new UngroupedTablesEvent(orderTableIds));
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> orderTables = tableTableGroupService.findOrderTableByIds(orderTableIds);
        tableGroupValidator.validateOrderTablesConditionForCreatingTableGroup(orderTables, orderTableIds.size());
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        publisher.publishEvent(new GroupedTablesEvent(orderTableIds, tableGroup.getId()));
        return TableGroupResponse.of(tableGroup);
    }
}

package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.event.GroupedTablesEvent;
import kitchenpos.common.event.UngroupedTablesEvent;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@Service
@Transactional
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupOrderTableService tableGroupOrderTableService;
    private final ApplicationEventPublisher publisher;
    private final TableGroupOrderValidator tableGroupOrderValidator;
    private final TableGroupOrderTableValidator tableGroupOrderTableValidator;

    public TableGroupService(final ApplicationEventPublisher publisher, final TableGroupOrderValidator tableGroupOrderValidator,
                             final TableGroupOrderTableService tableGroupOrderTableService,
                             final TableGroupOrderTableValidator tableGroupOrderTableValidator,
                             final TableGroupRepository tableGroupRepository) {
        this.publisher = publisher;
        this.tableGroupOrderValidator = tableGroupOrderValidator;
        this.tableGroupOrderTableService = tableGroupOrderTableService;
        this.tableGroupOrderTableValidator = tableGroupOrderTableValidator;
        this.tableGroupRepository = tableGroupRepository;
    }

    public void ungroup(final Long tableGroupId) {
        List<Long> orderTableIds = tableGroupOrderTableService.findOrderTableIdsByTableGroupId(tableGroupId);
        tableGroupOrderValidator.validateExistsOrdersStatusIsCookingOrMeal(orderTableIds);
        publisher.publishEvent(new UngroupedTablesEvent(orderTableIds));
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        tableGroupOrderTableValidator.validateOrderTablesConditionForCreatingTableGroup(orderTableIds);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        publisher.publishEvent(new GroupedTablesEvent(orderTableIds, tableGroup.getId()));
        return TableGroupResponse.of(tableGroup);
    }
}

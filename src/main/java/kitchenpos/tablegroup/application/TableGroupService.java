package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.GroupingPair;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.event.GroupEvent;
import kitchenpos.tablegroup.event.UngroupEvent;
import kitchenpos.tablegroup.exception.TableGroupNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(final TableGroupRepository tableGroupRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        validateOrderTablesSize(tableGroupRequest);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        applicationEventPublisher.publishEvent(new GroupEvent(new GroupingPair(tableGroup.getId(), tableGroupRequest.getOrderTableIds())));
        return TableGroupResponse.of(tableGroup);
    }

    private void validateOrderTablesSize(TableGroupRequest tableGroupRequest) {
        if (tableGroupRequest.isEmptyOrderTables() || tableGroupRequest.getOrderTablesSize() < 2) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableGroupNotFoundException(tableGroupId));
        applicationEventPublisher.publishEvent(new UngroupEvent(tableGroupId));
    }
}

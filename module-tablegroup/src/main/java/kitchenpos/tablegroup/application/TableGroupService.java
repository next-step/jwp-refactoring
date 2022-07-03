package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.event.GroupByEvent;
import kitchenpos.tablegroup.event.UngroupEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(TableGroupRepository tableGroupRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());

        applicationEventPublisher.publishEvent(
                GroupByEvent.of(tableGroup.getId(), tableGroupRequest.getOrderTableIds()));

        return TableGroupResponse.of(tableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        applicationEventPublisher.publishEvent(UngroupEvent.from(tableGroupId));
    }
}

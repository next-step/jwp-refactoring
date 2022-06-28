package kitchenpos.tableGroup.application;

import kitchenpos.ordertable.event.ReserveEvent;
import kitchenpos.ordertable.event.UngroupEvent;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final ApplicationEventPublisher applicationEventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        applicationEventPublisher.publishEvent(
                new ReserveEvent(savedTableGroup.id(), tableGroupRequest.getOrderTables()));
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        applicationEventPublisher.publishEvent(new UngroupEvent(tableGroupId));
    }
}

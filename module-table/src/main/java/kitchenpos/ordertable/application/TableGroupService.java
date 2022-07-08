package kitchenpos.ordertable.application;

import kitchenpos.ordertable.event.GroupTableEvent;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(final TableGroupRepository tableGroupRepository, ApplicationEventPublisher eventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        eventPublisher.publishEvent(GroupTableEvent.of(savedTableGroup.getId(), tableGroupRequest.getOrderTables()));
        return TableGroupResponse.from(savedTableGroup);
    }
}

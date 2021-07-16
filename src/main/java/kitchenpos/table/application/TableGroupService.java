package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.event.TableGroupCreatedEvent;
import kitchenpos.table.event.TableGroupUngroupEvent;
import kitchenpos.table.domain.TableGroup;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final OrderTableRepository orderTableRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final TableGroup tableGroup = new TableGroup();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        publisher.publishEvent(new TableGroupCreatedEvent(request, tableGroup));
        return TableGroupResponse.of(savedTableGroup,  orderTableRepository.findAllByTableGroupId(tableGroup.getId()));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        publisher.publishEvent(new TableGroupUngroupEvent(tableGroupId));
    }
}

package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.domain.repository.TableGroupRepository;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.event.tableGroup.TableGroupCreatedEvent;
import kitchenpos.order.event.tableGroup.TableGroupUngroupEvent;
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

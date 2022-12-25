package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupCreatedEvent;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.dto.TableGroupUngroupedEvent;

@Transactional(readOnly = true)
@Service
public class TableGroupService {
    private final OrderTableSupport orderTableSupport;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(
        final OrderTableSupport orderTableSupport,
        final TableGroupRepository tableGroupRepository,
        final ApplicationEventPublisher applicationEventPublisher
    ) {
        this.orderTableSupport = orderTableSupport;
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.generate());
        applicationEventPublisher.publishEvent(new TableGroupCreatedEvent(orderTableIds, savedTableGroup.getId()));

        List<OrderTableResponse> savedOrderTables = findSavedTables(orderTableIds);
        return TableGroupResponse.of(savedTableGroup, savedOrderTables);
    }

    private List<OrderTableResponse> findSavedTables(List<Long> orderTableIds) {
        return orderTableSupport.findOrderTables(orderTableIds);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        applicationEventPublisher.publishEvent(new TableGroupUngroupedEvent(tableGroupId));
    }
}

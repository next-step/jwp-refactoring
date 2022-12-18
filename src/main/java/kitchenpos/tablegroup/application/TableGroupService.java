package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupCreateEvent;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupUngroupEvent;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@Transactional(readOnly = true)
@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository,
        final ApplicationEventPublisher applicationEventPublisher
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.generate());
        applicationEventPublisher.publishEvent(new TableGroupCreateEvent(orderTableIds, savedTableGroup.getId()));

        List<OrderTable> savedOrderTables = findSavedTables(orderTableIds);
        return TableGroupResponse.of(savedTableGroup, savedOrderTables);
    }

    private List<OrderTable> findSavedTables(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        applicationEventPublisher.publishEvent(new TableGroupUngroupEvent(tableGroupId));
    }
}

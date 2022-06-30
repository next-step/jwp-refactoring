package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.event.TableUngroupedEvent;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.event.customEvent.TableUngroupEvent;
import kitchenpos.exception.TableGroupException;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final ApplicationEventPublisher eventPublisher;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;


    public TableGroupService(ApplicationEventPublisher eventPublisher,
        TableGroupRepository tableGroupRepository, OrderTableRepository orderTableRepository) {
        this.eventPublisher = eventPublisher;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new TableGroupException("단체테이블은 2개 이상이여야 합니다");
        }
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new TableGroupException("단체테이블은 2개 이상이여야 합니다");
            }
        }

        TableGroup tableGroup = new TableGroup();
        tableGroup = tableGroupRepository.save(tableGroup);
        final Long tableGroupId = tableGroup.getId();

        for (final OrderTable orderTable : orderTables) {
            orderTable.useTable();
            orderTable.mapToTableGroup(tableGroupId);
        }
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        TableUngroupedEvent tableUngroupedEvent = new TableUngroupedEvent(orderTableIds);
        eventPublisher.publishEvent(new TableUngroupEvent(tableUngroupedEvent));

        for (OrderTable orderTable : orderTables) {
            orderTable.unGroupTable();
        }
    }
}

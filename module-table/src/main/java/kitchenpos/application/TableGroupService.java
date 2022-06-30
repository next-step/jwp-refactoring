package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.event.TableUngroupedEvent;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.event.TableUngroupEvent;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);

        setOrderTables(orderTables, tableGroup);

        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    private void setOrderTables(List<OrderTable> orderTables, TableGroup tableGroup) {
        tableGroup.mapOrderTables(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.useTable();
            orderTable.mapToTableGroup(tableGroup);
        }
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

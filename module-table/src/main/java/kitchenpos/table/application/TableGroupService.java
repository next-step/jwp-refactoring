package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupEventPublisher tableGroupEventPublisher;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, TableGroupEventPublisher tableGroupEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupEventPublisher = tableGroupEventPublisher;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> savedOrderTables = findOrderTables(tableGroupRequest.findOrderTableIds());
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(savedOrderTables)));
        changeTableGroupId(savedOrderTables, savedTableGroup);
        return TableGroupResponse.of(savedTableGroup);
    }

    private void changeTableGroupId(List<OrderTable> savedOrderTables, TableGroup savedTableGroup) {
        for (OrderTable orderTable : savedOrderTables) {
            orderTable.changeTableGroupId(savedTableGroup.getId());
        }
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findOrderTables(List<Long> orderTableIds) {
        return orderTableRepository.findAllById(orderTableIds);
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(EntityNotFoundException::new);
        tableGroup.ungroup();
        tableGroupEventPublisher.publishUngroupTableEvent(TableGroupResponse.of(tableGroup));
    }

    public void cancelUngroup(final TableGroupResponse tableGroupResponse) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupResponse.getId())
            .orElseThrow(EntityNotFoundException::new);

        List<Long> orderTableIds = tableGroupResponse.getOrderTables().stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());
        List<OrderTable> savedOrderTables = findOrderTables(orderTableIds);
        tableGroup.group(savedOrderTables);
    }
}

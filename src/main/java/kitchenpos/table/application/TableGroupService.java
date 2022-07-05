package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.event.TableUngroupEvent;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private static final String ORDER_TABLE_IS_NOT_EXIST = "주문테이블이 존재하지 않습니다";
    private static final String TABLE_GROUP_IS_NOT_EXIST = "지정된 단체를 찾을 수 없습니다";
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableRepository orderTableRepository, ApplicationEventPublisher eventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findByIdIn(orderTableIds);
        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException(ORDER_TABLE_IS_NOT_EXIST);
        }
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.group(savedOrderTables));
        return new TableGroupResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findByIdWithOrderTable(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException(TABLE_GROUP_IS_NOT_EXIST));

        List<Long> orderTableIds = tableGroup.getGroupTables().getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        eventPublisher.publishEvent(new TableUngroupEvent(orderTableIds));

        tableGroup.ungroup();
    }
}

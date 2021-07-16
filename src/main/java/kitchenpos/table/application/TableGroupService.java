package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableUngroupedEvent;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private static final String TABLE_GROUP_NOT_EXIST = "테이블그룹이 존재하지 않습니다";

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTables();
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        TableGroup tableGroup = TableGroup.createWithIdValidation(orderTableIds, new OrderTables(orderTables));
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return new TableGroupResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableGroupNotFoundException(TABLE_GROUP_NOT_EXIST));
        applicationEventPublisher.publishEvent(new TableUngroupedEvent(tableGroup.getOrderTableIds()));
        tableGroup.ungroup();
    }
}

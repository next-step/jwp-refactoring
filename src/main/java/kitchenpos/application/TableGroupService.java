package kitchenpos.application;

import kitchenpos.domain.domainEvents.GroupOrderTableEvent;
import kitchenpos.domain.tableGroup.*;
import kitchenpos.domain.tableGroup.exceptions.InvalidTableGroupTryException;
import kitchenpos.domain.tableGroup.exceptions.TableGroupEntityNotFoundException;
import kitchenpos.ui.dto.tableGroup.OrderTableInTableGroupRequest;
import kitchenpos.ui.dto.tableGroup.TableGroupRequest;
import kitchenpos.ui.dto.tableGroup.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final SafeOrderTableInTableGroup safeOrderTableInTableGroup;
    private final SafeOrderInTableGroup safeOrderInTableGroup;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(
            final SafeOrderTableInTableGroup safeOrderTableInTableGroup,
            final SafeOrderInTableGroup safeOrderInTableGroup,
            final TableGroupRepository tableGroupRepository,
            final ApplicationEventPublisher applicationEventPublisher
    ) {
        this.safeOrderTableInTableGroup = safeOrderTableInTableGroup;
        this.safeOrderInTableGroup = safeOrderInTableGroup;
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse group(final TableGroupRequest tableGroupRequest) {
        validateGroup(tableGroupRequest);

        TableGroup tableGroup = parseToTableGroup(tableGroupRequest);
        TableGroup saved = tableGroupRepository.save(tableGroup);

        applicationEventPublisher.publishEvent(new GroupOrderTableEvent(parseToOrderTableIds(tableGroupRequest)));

        // TODO: 여기서 주문 테이블 정보 불러와서 보내도록 변경 필요
        return TableGroupResponse.of(saved);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableGroupEntityNotFoundException("존재하지 않는 단체 지정입니다."));

        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTableInTableGroup::getOrderTableId)
                .collect(Collectors.toList());

        safeOrderInTableGroup.canUngroup(orderTableIds);

        tableGroupRepository.delete(tableGroup);
    }

    private void validateGroup(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = parseToOrderTableIds(tableGroupRequest);

        if (tableGroupRepository.existsByOrderTablesOrderTableIdIn(orderTableIds)) {
            throw new InvalidTableGroupTryException("이미 단체 지정된 주문 테이블을 단체 지정할 수 없습니다.");
        }

        safeOrderTableInTableGroup.canGroupTheseTables(orderTableIds);
    }

    private TableGroup parseToTableGroup(final TableGroupRequest tableGroupRequest) {
        List<OrderTableInTableGroup> orderTablesInTableGroup = tableGroupRequest.getOrderTables().stream()
                .map(it -> new OrderTableInTableGroup(it.getId()))
                .collect(Collectors.toList());
        return new TableGroup(LocalDateTime.now(), orderTablesInTableGroup);
    }

    private List<Long> parseToOrderTableIds(final TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables().stream()
                .map(OrderTableInTableGroupRequest::getId)
                .collect(Collectors.toList());
    }
}

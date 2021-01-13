package kitchenpos;

import dto.tablegroup.OrderTableInTableGroupRequest;
import dto.tablegroup.TableGroupRequest;
import dto.tablegroup.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final kitchenpos.domain.tablegroup.SafeOrderTableInTableGroup safeOrderTableInTableGroup;
    private final kitchenpos.domain.tablegroup.SafeOrderInTableGroup safeOrderInTableGroup;
    private final kitchenpos.domain.tablegroup.TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(
            final kitchenpos.domain.tablegroup.SafeOrderTableInTableGroup safeOrderTableInTableGroup,
            final kitchenpos.domain.tablegroup.SafeOrderInTableGroup safeOrderInTableGroup,
            final kitchenpos.domain.tablegroup.TableGroupRepository tableGroupRepository,
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

        kitchenpos.domain.tablegroup.TableGroup tableGroup = parseToTableGroup(tableGroupRequest);
        kitchenpos.domain.tablegroup.TableGroup saved = tableGroupRepository.save(tableGroup);

        applicationEventPublisher.publishEvent(new kitchenpos.domain.domainevents.GroupOrderTableEvent(parseToOrderTableIds(tableGroupRequest)));

        // TODO: 여기서 주문 테이블 정보 불러와서 보내도록 변경 필요
        return TableGroupResponse.of(saved);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        kitchenpos.domain.tablegroup.TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new kitchenpos.domain.tablegroup.exceptions.TableGroupEntityNotFoundException("존재하지 않는 단체 지정입니다."));

        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(kitchenpos.domain.tablegroup.OrderTableInTableGroup::getOrderTableId)
                .collect(Collectors.toList());

        safeOrderInTableGroup.canUngroup(orderTableIds);

        tableGroupRepository.delete(tableGroup);
    }

    private void validateGroup(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = parseToOrderTableIds(tableGroupRequest);

        if (tableGroupRepository.findTableGroupsInOrderTableIds(orderTableIds).size() != 0) {
            throw new kitchenpos.domain.tablegroup.exceptions.InvalidTableGroupTryException("이미 단체 지정된 주문 테이블을 단체 지정할 수 없습니다.");
        }

        safeOrderTableInTableGroup.canGroupTheseTables(orderTableIds);
    }

    private kitchenpos.domain.tablegroup.TableGroup parseToTableGroup(final TableGroupRequest tableGroupRequest) {
        List<kitchenpos.domain.tablegroup.OrderTableInTableGroup> orderTablesInTableGroup = tableGroupRequest.getOrderTables().stream()
                .map(it -> new kitchenpos.domain.tablegroup.OrderTableInTableGroup(it.getId()))
                .collect(Collectors.toList());
        return new kitchenpos.domain.tablegroup.TableGroup(LocalDateTime.now(), orderTablesInTableGroup);
    }

    private List<Long> parseToOrderTableIds(final TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables().stream()
                .map(OrderTableInTableGroupRequest::getId)
                .collect(Collectors.toList());
    }
}

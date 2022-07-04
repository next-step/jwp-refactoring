package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.validator.TableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
                             TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = findOrderTablesByIds(tableGroupRequest.getOrderTableIds());
        validateJoinedTableGroup(tableGroupRequest.getOrderTableIds());
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        return TableGroupResponse.of(savedTableGroup);
    }

    private List<OrderTable> findOrderTablesByIds(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("등록되지 않은 테이블이 있습니다.");
        }
        return orderTables;
    }

    private void validateJoinedTableGroup(List<Long> orderTableIds) {
        if (tableGroupRepository.existsByOrderTableIdIn(orderTableIds)) {
            throw new IllegalArgumentException("이미 그룹에 속한 테이블이 있습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        validateOrderStatus(tableGroup.getOrderTables());
        tableGroup.ungroup();
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블그룹입니다."));
    }

    private void validateOrderStatus(OrderTables orderTables) {
        orderTables.getOrderTableIds()
                .forEach(tableValidator::validateOrderStatus);
    }
}

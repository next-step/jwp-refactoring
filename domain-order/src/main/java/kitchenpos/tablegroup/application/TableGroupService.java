package kitchenpos.tablegroup.application;

import static java.time.LocalDateTime.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupId;
import kitchenpos.table.domain.TableUngroupValidator;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@Service
public class TableGroupService {
    private final TableUngroupValidator tableUngroupValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        final TableUngroupValidator tableUngroupValidator,
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.tableUngroupValidator = tableUngroupValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = findOrderTables(tableGroupRequest);
        final TableGroup persistTableGroup = tableGroupRepository.save(new TableGroup(now()));
        persistTableGroup.group(orderTables);
        return TableGroupResponse.of(persistTableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("등록된 테이블 그룹만 그룹해제 가능합니다."));
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(new TableGroupId(tableGroupId));
        tableGroup.ungroup(orderTables, tableUngroupValidator);
    }

    private List<OrderTable> findOrderTables(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("등록이 되지 않은 주문테이블은 그룹화 할 수 없습니다.");
        }
        return orderTables;
    }
}

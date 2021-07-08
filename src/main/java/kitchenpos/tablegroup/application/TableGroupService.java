package kitchenpos.tablegroup.application;

import static java.time.LocalDateTime.*;

import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                            final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = findOrderTables(tableGroupRequest);
        final TableGroup persistTableGroup = tableGroupRepository.save(TableGroup.create(orderTables, now()));
        return TableGroupResponse.of(persistTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("등록된 테이블 그룹만 그룹해제 가능합니다."));
        tableGroup.ungroup();
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

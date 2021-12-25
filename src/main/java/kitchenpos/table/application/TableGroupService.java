package kitchenpos.table.application;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = findOrderTablesById(tableGroupRequest.getOrderTableIds());

        final TableGroup tableGroup = TableGroup.create();
        tableGroup.group(orderTables);

        final TableGroup persistTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.of(persistTableGroup);
    }


    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup persistTableGroup = findTableGroupById(tableGroupId);
        persistTableGroup.ungroup();
    }


    private List<OrderTable> findOrderTablesById(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);

        checkOrderTableValidation(orderTableIds, orderTables);

        return orderTables;
    }

    private void checkOrderTableValidation(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("요청한 그룹화 주문 테이블 갯수와 저장된 주문 테이블 갯수가 일치하지 않습니다.");
        }
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundException("해당 테이블 그룹을 찾을 수 없습니다."));
    }
}

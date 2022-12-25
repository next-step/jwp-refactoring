package kitchenpos.table.application;

import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableGroupService {
    private final ExistsOrderPort existsOrderPort;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final ExistsOrderPort existsOrderPort, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.existsOrderPort = existsOrderPort;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> savedOrderTables = findAllOrderTableByIds(orderTableIds);
        TableGroupCreateValidator.validate(orderTableIds, savedOrderTables);
        return TableGroupResponse.of(tableGroupRepository.save(TableGroupRequest.toTableGroup()));
    }

    private List<OrderTable> findAllOrderTableByIds(List<Long> orderTableIds) {
        return orderTableRepository.findAllById(orderTableIds);
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        boolean completedOrderTable = existsByOrderTableIdInAndOrderStatusIn(tableGroup.getOrderTableIds());
        TableGroupUnGroupValidator.validate(completedOrderTable);
        tableGroup.ungroup();
    }

    private TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("단체가 존재하지 않습니다."));
    }

    private boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds) {
        return existsOrderPort.existsOrderStatusCookingOrMeal(orderTableIds);
    }
}

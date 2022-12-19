package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupCreateValidator;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableGroupUnGroupValidator;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ExistsOrderPort existsOrderPort;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository,
            ExistsOrderPort existsOrderPort) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.existsOrderPort = existsOrderPort;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> savedOrderTables = findAllOrderTableByIds(orderTableIds);
        TableGroupCreateValidator.validate(orderTableIds, savedOrderTables);
        TableGroup tableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup(savedOrderTables));
        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        boolean completedOrderTable = existsOrderStatusCookingOrMeal(tableGroup.getOrderTableIds());
        TableGroupUnGroupValidator.validate(completedOrderTable);
        tableGroup.ungroup();
    }

    private List<OrderTable> findAllOrderTableByIds(List<Long> orderTableIds) {
        return orderTableRepository.findAllById(orderTableIds);
    }

    private TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("단체가 존재하지 않습니다."));
    }

    private boolean existsOrderStatusCookingOrMeal(List<Long> orderTableIds) {
        return existsOrderPort.existsOrderStatusCookingOrMeal(orderTableIds);
    }
}

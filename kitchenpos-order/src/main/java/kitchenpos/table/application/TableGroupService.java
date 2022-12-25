package kitchenpos.table.application;

import kitchenpos.common.ErrorMessage;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableGroupService {
    private final TableValidator tableValidator;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupService(TableValidator tableValidator, TableGroupRepository tableGroupRepository, OrderTableRepository orderTableRepository) {
        this.tableValidator = tableValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        List<OrderTable> orderTables = findAllOrderTableByIds(request.getOrderTables());
        return TableGroupResponse.of(tableGroupRepository.save(TableGroupRequest.toTableGroup(orderTables)));
    }

    private List<OrderTable> findAllOrderTableByIds(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_TABLE_NOT_FOUND_BY_ID.getMessage());
        }
        return orderTables;
    }

    public void ungroup(Long tableGroupId) {
        TableGroup saveTableGroup = findTableGroupById(tableGroupId);
        tableValidator.validateUngroup(saveTableGroup.getOrderTableIds());
        saveTableGroup.ungroup();
        tableGroupRepository.save(saveTableGroup);
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.TABLE_GROUP_NOT_FOUND_BY_ID.getMessage()));
    }
}

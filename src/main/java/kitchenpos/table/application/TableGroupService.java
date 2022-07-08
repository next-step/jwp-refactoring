package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.TableException;
import kitchenpos.table.exception.TableExceptionType;
import kitchenpos.table.validator.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> savedOrderTables = getOrderTables(tableGroupRequest.fetchOrderTableIds());
        final TableGroup tableGroup = TableGroup.of(savedOrderTables, tableGroupRequest.size());

        final TableGroup save = tableGroupRepository.save(tableGroup);
        save.updateGroupTableId();

        return TableGroupResponse.of(save);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findByTableGroupId(tableGroupId);
        final List<OrderTable> savedOrderTables = getOrderTables(tableGroup.getOrderTableIds());
        orderTableValidator.validateTableSeparate(savedOrderTables);
        tableGroup.ungroupTable();
    }

    private TableGroup findByTableGroupId(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableException(TableExceptionType.TABLE__GROUP_NOT_FOUND));
    }

    private List<OrderTable> getOrderTables(final List<Long> groupIds) {
        return orderTableRepository.findAllById(groupIds);
    }
}

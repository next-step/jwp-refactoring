package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.exception.TableException;
import kitchenpos.exception.TableExceptionType;
import kitchenpos.validator.OrderStatusValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderStatusValidator orderStatusValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository,
                             final OrderStatusValidator orderStatusValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderStatusValidator = orderStatusValidator;
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
        orderStatusValidator.validateTableSeparate(savedOrderTables);
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

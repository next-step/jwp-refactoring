package kitchenpos.table.application;

import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.validator.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository,
            final OrderTableValidator orderTableValidator) {
        this.orderTableValidator = orderTableValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }


    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final OrderTables savedOrderTables = OrderTables.ofCreate(orderTableRepository.findAllByIdIn(orderTableIds));
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        savedOrderTables.initTableGroup(savedTableGroup.getId(), orderTableIds);
        return TableGroupResponse.of(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables savedOrderTables = OrderTables.ofUngroup(
                orderTableRepository.findAllByTableGroupId(tableGroupId));
        orderTableValidator.checkTablesOrderStatus(savedOrderTables);
        savedOrderTables.ungroup();
        tableGroupRepository.deleteById(tableGroupId);
    }

    public int countById(final Long tableGroupId) {
        return tableGroupRepository.countById(tableGroupId);
    }
}

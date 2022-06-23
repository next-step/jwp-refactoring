package kitchenpos.application.tablegroup;

import java.util.List;
import kitchenpos.application.table.OrderTableService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderTableService orderTableService;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(final OrderTableService orderTableService,
                             final TableGroupRepository tableGroupRepository,
                             final TableGroupValidator tableGroupValidator) {
        this.orderTableService = orderTableService;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = tableGroupValidator.validateReturnOrderTables(tableGroupRequest.getOrderTableIds());

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());
        tableGroup.assignedOrderTables(orderTables);

        return TableGroupResponse.of(tableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableService.findAllByTableGroupId(tableGroupId);
        tableGroupValidator.validateUngroup(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }
}

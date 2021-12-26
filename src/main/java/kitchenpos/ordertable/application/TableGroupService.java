package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import kitchenpos.ordertable.exception.TableGroupNotFoundException;
import kitchenpos.validator.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository,
        TableService tableService, OrderTableValidator orderTableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableRequest> orderTableRequests = tableGroupRequest.getOrderTables();
        List<OrderTable> orderTables = tableService.findOrderTables(orderTableRequests);

        TableGroup tableGroup = new TableGroup(orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroup(tableGroupId);
        List<OrderTable> orderTableList = tableGroup.getOrderTableList();
        orderTableList.stream()
            .forEach(orderTable -> orderTableValidator.validateAllOrdersInTableComplete(
                orderTable.getId()));

        orderTableList.stream()
            .forEach(orderTable -> orderTable.unGroup());

        tableGroup.ungroup();
    }

    public TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(TableGroupNotFoundException::new);
    }
}
